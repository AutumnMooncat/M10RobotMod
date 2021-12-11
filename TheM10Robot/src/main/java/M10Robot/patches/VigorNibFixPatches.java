package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.PenNib;

public class VigorNibFixPatches {

    @SpirePatch(clz = VigorPower.class, method = "onUseCard")
    public static class VigorFix {
        public static void Postfix(VigorPower __instance, AbstractCard card, UseCardAction action) {
            if (card.type != AbstractCard.CardType.ATTACK && BoosterFieldPatch.hasDamageDealingBooster(card)) {
                __instance.flash();
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, __instance));
            }
        }
    }

    @SpirePatch(clz = PenNib.class, method = "onUseCard")
    public static class PenNibFix {
        public static void Postfix(PenNib __instance, AbstractCard card, UseCardAction action) {
            if (card.type != AbstractCard.CardType.ATTACK && BoosterFieldPatch.hasDamageDealingBooster(card)) {
                ++__instance.counter;
                if (__instance.counter == 10) {
                    __instance.counter = 0;
                    __instance.flash();
                    __instance.stopPulse();
                } else if (__instance.counter == 9) {
                    __instance.beginLongPulse();
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PenNibPower(AbstractDungeon.player, 1), 1, true));
                }
            }
        }
    }

    @SpirePatch(clz = PenNibPower.class, method = "onUseCard")
    public static class PenNibPowerFix {
        public static void Postfix(PenNibPower __instance, AbstractCard card, UseCardAction action) {
            if (card.type != AbstractCard.CardType.ATTACK && BoosterFieldPatch.hasDamageDealingBooster(card)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, __instance));
            }
        }
    }

}
