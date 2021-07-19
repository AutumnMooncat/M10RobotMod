package M10Robot.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class CostBypassFieldPatches {
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class OverrideEnergy {
        public static SpireReturn<?> Prefix(AbstractCard __instance) {
            if (CostBypassField.bypassCost.get(__instance)) {
                if (!AbstractDungeon.actionManager.turnHasEnded) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderNewColor {
        @SpireInsertPatch(locator = Locator.class, localvars = "costColor")
        public static void pls(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor) {
            if (CostBypassField.bypassCost.get(__instance)) {
                costColor[0] = BoosterFieldPatch.boosterColor;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "getCost");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            CostBypassField.bypassCost.set(result, CostBypassField.bypassCost.get(self));
            return result;
        }
    }
}
