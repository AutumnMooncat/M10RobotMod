package M10Robot.patches;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.cards.interfaces.OnOverclockCard;
import M10Robot.util.interfaces.OverclockBeforePlayItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

import java.util.ArrayList;

public class OverclockPatches {
    private static final int PERCENT = 25;
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class OverclockField {
        public static SpireField<Integer> overclocks = new SpireField<>(() -> 0);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            OverclockField.overclocks.set(result, OverclockField.overclocks.get(self));
            return result;
        }
    }

    public static void overclock(AbstractCard c, int amount) {
        OverclockField.overclocks.set(c, OverclockField.overclocks.get(c) + amount);
        if (c instanceof OnOverclockCard) {
            ((OnOverclockCard) c).onOverclock(amount);
        }
        c.applyPowers();
        c.initializeDescription();
        //c.superFlash();
    }

    public static boolean canOverclock(AbstractCard c) {
        return !(c instanceof CannotOverclock) && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE;
    }

    public static int getOverClockPercent(AbstractCard c) {
        int amount = OverclockField.overclocks.get(c);
        if (isCombatCard(c)) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof OverclockBeforePlayItem) {
                    amount += ((OverclockBeforePlayItem) p).overclockAmount(c);
                }
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OverclockBeforePlayItem) {
                    amount += ((OverclockBeforePlayItem) r).overclockAmount(c);
                }
            }
        }
        return amount * PERCENT;
    }

    public static void onApplyPowers(AbstractCard card) {
        int rawPercent = getOverClockPercent(card);
        card.magicNumber = (int) (card.baseMagicNumber * (100F + rawPercent) / 100F);
        card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
        if (card instanceof AbstractModdedCard) {
            ((AbstractModdedCard) card).secondMagicNumber = (int) (((AbstractModdedCard) card).baseSecondMagicNumber * (100F + rawPercent) / 100F);
            ((AbstractModdedCard) card).isSecondMagicNumberModified = ((AbstractModdedCard) card).secondMagicNumber != ((AbstractModdedCard) card).baseSecondMagicNumber;
        }
    }

    public static float onModifyDamageFinal(float damage, AbstractCard card) {
        return (damage * (100F + getOverClockPercent(card))) / 100F;
    }

    public static float onModifyBlockFinal(float block, AbstractCard card) {
        return (block * (100F + getOverClockPercent(card))) / 100F;
    }

    public static void onRemove(AbstractCard card) {
        card.magicNumber = card.baseMagicNumber;
        card.isMagicNumberModified = false;
        if (card instanceof AbstractModdedCard) {
            ((AbstractModdedCard) card).secondMagicNumber = ((AbstractModdedCard) card).baseSecondMagicNumber;
            ((AbstractModdedCard) card).isSecondMagicNumberModified = false;
        }
        card.applyPowers();
        card.initializeDescription();
    }

    private static boolean isCombatCard(AbstractCard c) {
        return AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.masterDeck.contains(c);
    }

    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class OnUseCard {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (!card.dontTriggerOnUseCard) {
                int appliedOnPlay = 0;
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof OverclockBeforePlayItem) {
                        appliedOnPlay += ((OverclockBeforePlayItem) p).overclockAmount(card);
                        ((OverclockBeforePlayItem) p).onOverclock(card);
                    }
                }
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof OverclockBeforePlayItem) {
                        appliedOnPlay += ((OverclockBeforePlayItem) r).overclockAmount(card);
                        ((OverclockBeforePlayItem) r).onOverclock(card);
                    }
                }
                if (appliedOnPlay > 0 && card instanceof OnOverclockCard) {
                    ((OnOverclockCard) card).onOverclock(appliedOnPlay);
                }
                OverclockField.overclocks.set(card, 0);
                onRemove(card);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderTitle")
    public static class renderOverclockPercent {
        @SpirePostfixPatch
        public static void renderPlz(AbstractCard __instance, SpriteBatch sb, Color ___renderColor) {
            if (AbstractDungeon.player != null && getOverClockPercent(__instance) > 0) {
                Color color = Settings.GREEN_TEXT_COLOR.cpy();
                color.a = ___renderColor.a;
                FontHelper.cardTitleFont.getData().setScale(__instance.drawScale);
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, "+"+getOverClockPercent(__instance)+"%", __instance.current_x, __instance.current_y, 0.0F, 195.0F * __instance.drawScale * Settings.scale, __instance.angle, false, color);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class OnApplyPowers {
        public static void Postfix(AbstractCard __instance) {
            onApplyPowers(__instance);
        }

        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = {"tmp"})
        public static void damageFinalInsert(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = onModifyDamageFinal(tmp[0], __instance);
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp", "i"})
        public static void multiDamageFinalInsert(AbstractCard __instance, float[] tmp, int i) {
            tmp[i] = onModifyDamageFinal(tmp[i], __instance);
        }

        private static class MultiDamageFinalLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{tmp[3]};
            }
        }

        private static class DamageFinalLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{tmp[1]};
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class ApplyPowersToBlock {
        @SpireInsertPatch(locator = BlockFinalLocator.class, localvars = {"tmp"})
        public static void blockFinalInsert(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = onModifyBlockFinal(tmp[0], __instance);
        }

        private static class BlockFinalLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CardModifierCalculateCardDamage {

        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = {"tmp"})
        public static void damageFinalInsert(AbstractCard __instance, AbstractMonster m, @ByRef float[] tmp) {
            tmp[0] = onModifyDamageFinal(tmp[0], __instance);
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp", "i", "m"})
        public static void multiDamageFinalInsert(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i, ArrayList<AbstractMonster> m) {
            tmp[i] = onModifyDamageFinal(tmp[i], __instance);
        }

        private static class MultiDamageFinalLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{tmp[3]};
            }
        }

        private static class DamageFinalLocator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{tmp[1]};
            }
        }
    }
}
