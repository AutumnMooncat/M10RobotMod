package M10Robot.cutStuff.patches;

import M10Robot.cutStuff.AbstractBoosterCard;
import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.interfaces.NegativePrimaryEffect;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CtBehavior;

public class HandCardSelectScreenPatches {

    @SpirePatch(clz = HandCardSelectScreen.class, method = SpirePatch.CLASS)
    public static class PreviewWithBoosterField {
        public static SpireField<Boolean> previewBooster = new SpireField<>(() -> Boolean.FALSE);
        public static SpireField<AbstractBoosterCard> booster = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = HandCardSelectScreen.class, method = "updateMessage")
    @SpirePatch(clz = HandCardSelectScreen.class, method = "selectHoveredCard")
    public static class ShowBoostedEffectPls {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> stop(HandCardSelectScreen __instance) {
            AbstractBoosterCard booster = PreviewWithBoosterField.booster.get(__instance);
            AbstractCard modifiedCard = __instance.upgradePreviewCard;
            AbstractCard selectedCard = __instance.selectedCards.group.get(0);
            if (PreviewWithBoosterField.previewBooster.get(__instance) && booster != null) {
                M10Robot.patches.BoosterFieldPatch.betterEquipBooster(modifiedCard, booster);
                modifiedCard.drawScale = 0.75F;
                modifiedCard.targetDrawScale = 0.75F;
                modifiedCard.isDamageModified = selectedCard.damage != modifiedCard.damage;
                modifiedCard.isBlockModified = selectedCard.block != modifiedCard.block;
                modifiedCard.isMagicNumberModified = selectedCard.magicNumber != modifiedCard.magicNumber;
                modifiedCard.isCostModified = selectedCard.cost != modifiedCard.cost;
                if (modifiedCard instanceof AbstractModdedCard && selectedCard instanceof AbstractModdedCard && modifiedCard instanceof NegativePrimaryEffect) {
                    int delta = modifiedCard.magicNumber - selectedCard.magicNumber;
                    if (delta != 0) {
                        ((AbstractModdedCard) modifiedCard).baseSecondMagicNumber = -1;
                        ((AbstractModdedCard) modifiedCard).secondMagicNumber = ((AbstractModdedCard) selectedCard).secondMagicNumber - delta;
                        if (((AbstractModdedCard) modifiedCard).secondMagicNumber < 0) {
                            if (((NegativePrimaryEffect) modifiedCard).reboundOnNegative()) {
                                ((AbstractModdedCard) modifiedCard).secondMagicNumber = -((AbstractModdedCard) modifiedCard).secondMagicNumber;
                            } else {
                                ((AbstractModdedCard) modifiedCard).secondMagicNumber = 0;
                            }
                        }
                        ((AbstractModdedCard) modifiedCard).isSecondMagicNumberModified = true;
                    }
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
