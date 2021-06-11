package M10Robot.patches;

import M10Robot.powers.interfaces.OnRemoveOtherPowerPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class OnRemoveOtherPowerPatch {

    @SpirePatch(
            clz = RemoveSpecificPowerAction.class,
            method = "update"

    )
    public static class RemovePowerListener {
        @SpireInsertPatch(locator = Locator.class, localvars = {"removeMe"})
        public static SpireReturn<?> removePowerListener(RemoveSpecificPowerAction __instance, AbstractPower removeMe) {
            for (AbstractPower p : removeMe.owner.powers) {
                if (p instanceof OnRemoveOtherPowerPower && p != removeMe) {
                    ((OnRemoveOtherPowerPower) p).onRemoveOtherPower(removeMe);
                }
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "onModifyPower");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
