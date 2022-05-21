package M10Robot.patches;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.cards.interfaces.OnOverclockCard;
import M10Robot.util.interfaces.OverclockBeforePlayItem;
import basemod.Pair;
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
        public static SpireField<Integer> percentLastFrame = new SpireField<>(() -> 0);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            //OverclockField.overclocks.set(result, OverclockField.overclocks.get(self));
            overclock(result, OverclockField.overclocks.get(self));
            return result;
        }
    }

    public static void overclock(AbstractCard c, int amount) {
        if (amount != 0) {
            if (!canOverclock(c)) {
                return;
            }
            OverclockField.overclocks.set(c, OverclockField.overclocks.get(c) + amount);
            if (c instanceof OnOverclockCard) {
                ((OnOverclockCard) c).onOverclock(amount);
            }
            c.applyPowers();
            c.initializeDescription();
            //c.superFlash();
        }

    }

    public static boolean canOverclock(AbstractCard c) {
        return !(c instanceof CannotOverclock) && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE;
    }

    public static int getOverClockPercent(AbstractCard c) {
        int amount = OverclockField.overclocks.get(c);
        if (isCombatCard(c) && canOverclock(c)) {
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
        if (rawPercent != 0) {
            card.magicNumber = (int) Math.max(0, card.baseMagicNumber * (100F + rawPercent) / 100F);
            card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
            if (card instanceof AbstractModdedCard) {
                ((AbstractModdedCard) card).secondMagicNumber = (int) Math.max(0,((AbstractModdedCard) card).baseSecondMagicNumber * (100F + rawPercent) / 100F);
                ((AbstractModdedCard) card).isSecondMagicNumberModified = ((AbstractModdedCard) card).secondMagicNumber != ((AbstractModdedCard) card).baseSecondMagicNumber;
            }
        }
        if (!OverclockField.percentLastFrame.get(card).equals(rawPercent)) {
            OverclockField.percentLastFrame.set(card, rawPercent);
            card.initializeDescription();
        }
    }

    public static void fixMagicNumbers() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.magicNumber = c.baseMagicNumber;
            c.isMagicNumberModified = false;
            if (c instanceof AbstractModdedCard) {
                ((AbstractModdedCard) c).secondMagicNumber = ((AbstractModdedCard) c).baseSecondMagicNumber;
                ((AbstractModdedCard) c).isSecondMagicNumberModified = false;
            }
        }
        AbstractDungeon.player.onCardDrawOrDiscard();
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
        return AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hand.contains(c);
    }

    @SpirePatch(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class OnUseCard {
        @SpirePrefixPatch
        public static void Insert(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (!(card instanceof CannotOverclock)) {
                int appliedOnPlay = 0;
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof OverclockBeforePlayItem) {
                        appliedOnPlay += ((OverclockBeforePlayItem) p).overclockAmount(card);
                    }
                }
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r instanceof OverclockBeforePlayItem) {
                        appliedOnPlay += ((OverclockBeforePlayItem) r).overclockAmount(card);
                    }
                }
                if (!card.dontTriggerOnUseCard) {
                    for (AbstractPower p : AbstractDungeon.player.powers) {
                        if (p instanceof OverclockBeforePlayItem) {
                            ((OverclockBeforePlayItem) p).onOverclock(card);
                        }
                    }
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        if (r instanceof OverclockBeforePlayItem) {
                            ((OverclockBeforePlayItem) r).onOverclock(card);
                        }
                    }
                }
                if (appliedOnPlay > 0) {
                    fixMagicNumbers();
                    overclock(card, appliedOnPlay);
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "clearPowers")
    public static class RemoveOverclocks {
        @SpirePrefixPatch
        public static void plz(AbstractCard __instance) {
            OverclockField.overclocks.set(__instance, 0);
            if (__instance instanceof AbstractSwappableCard && __instance.cardsToPreview != null) {
                OverclockField.overclocks.set(__instance.cardsToPreview, 0);
            }
            onRemove(__instance);
        }
    }

    private static final Color M = new Color(1, 0, 1, 1);
    private static final Color R = new Color(1, 0, 0, 1);
    private static final Color Y = new Color(1, 1, 0, 1);
    private static final Color G = new Color(0, 1, 0, 1);
    private static final Color C = new Color(0, 1, 1, 1);
    private static final Color B = new Color(0, 0, 1, 1);
    private static final Pair<Color, Color> p300 = new Pair<>(R, M);
    private static final Pair<Color, Color> p200 = new Pair<>(Y, R);
    private static final Pair<Color, Color> p100 = new Pair<>(G, Y);
    private static final Pair<Color, Color> p50 = new Pair<>(C, G);
    private static final Pair<Color, Color> p0 = new Pair<>(B, C);

    private static Color getOverclockColor(int percent) {
        Pair<Color, Color> colors = getColors(percent);
        //float grad = getGrad(percent);
        return colors.getKey().cpy().lerp(colors.getValue(), getGrad(percent));
        //return new Color(colors.getKey().r*(1-grad)+colors.getValue().r*grad, colors.getKey().g*(1-grad)+colors.getValue().g*grad, colors.getKey().b*(1-grad)+colors.getValue().b*grad, 1);
    }

    private static float getGrad(int percent) {
        percent += 100;
        if (percent >= 500) {
            return 1;
        } else if (percent >= 300) {
            return (percent-300)/200f;
        } else if (percent >= 200) {
            return (percent-200)/100f;
        } else if (percent >= 100) {
            return (percent-100)/100f;
        } else if (percent >= 50) {
            return (percent-50)/50f;
        } else {
            return percent/50f;
        }
    }

    private static Pair<Color, Color> getColors(int percent) {
        percent += 100;
        if (percent >= 300) {
            return p300;
        } else if (percent >= 200) {
            return p200;
        } else if (percent >= 100) {
            return p100;
        } else if (percent >= 50) {
            return p50;
        } else {
            return p0;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderTitle")
    public static class renderOverclockPercent {
        @SpirePostfixPatch
        public static void renderPlz(AbstractCard __instance, SpriteBatch sb, Color ___renderColor) {
            int percent = getOverClockPercent(__instance);
            if (AbstractDungeon.player != null && percent != 0) {
                Color color = getOverclockColor(percent);
                color.a = ___renderColor.a;
                FontHelper.cardTitleFont.getData().setScale(__instance.drawScale);
                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, Math.max(0, 100+percent)+"%", __instance.current_x, __instance.current_y, 0.0F, 195.0F * __instance.drawScale * Settings.scale, __instance.angle, false, color);
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
