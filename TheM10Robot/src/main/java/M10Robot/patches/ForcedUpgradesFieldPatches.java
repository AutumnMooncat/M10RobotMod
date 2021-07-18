package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

public class ForcedUpgradesFieldPatches {
    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class PlayExtraCopies {
        @SpireInsertPatch(locator = Locator.class, localvars = "card")
        public static void withoutInfiniteLoopPls(AbstractCard __instance, AbstractCard card) {
            //This takes place before we call card.upgrade(). If we forced an upgrade, the timesUpgraded variable will be more than 1, but most cards check !upgraded in the upgrade code.
            if (ForcedUpgradeField.wasForced.get(__instance)) {
                //Therefore. we set upgraded to false before we call upgrade each time to ensure it upgrades everything correctly
                card.upgraded = false;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            ForcedUpgradeField.wasForced.set(result, ForcedUpgradeField.wasForced.get(self));
            return result;
        }
    }

}
