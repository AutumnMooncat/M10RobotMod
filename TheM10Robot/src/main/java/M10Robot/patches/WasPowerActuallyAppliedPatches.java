package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class WasPowerActuallyAppliedPatches {

    @SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class AppliedField {
        public static SpireField<Boolean> actuallyApplied = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz = ApplyPowerAction.class, method = "update")
    public static class UpdateActuallyApplied {
        @SpireInsertPatch(locator = Locator.class)
        public static void setApplied(ApplyPowerAction __instance) {
            AppliedField.actuallyApplied.set(__instance, true);
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "onModifyPower");
            return LineFinder.findAllInOrder(ctBehavior, matcher);
        }
    }

}
