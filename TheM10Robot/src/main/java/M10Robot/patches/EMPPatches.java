package M10Robot.patches;

import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.powers.EMPPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EMPPatches {
    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
    public static class DontRemovePower {
        @SpirePrefixPatch
        public static SpireReturn<?> dontRemove(RemoveSpecificPowerAction __instance, AbstractPower ___powerInstance) {
            if (___powerInstance != null && __instance.target != null && ___powerInstance.type == AbstractPower.PowerType.DEBUFF && __instance.target.hasPower(EMPPower.POWER_ID) && !(___powerInstance instanceof EMPPower)) {
                __instance.target.getPower(EMPPower.POWER_ID).flash();
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ReducePowerAction.class, method = "update")
    public static class DontReducePower {
        @SpirePrefixPatch
        public static SpireReturn<?> dontReduce(ReducePowerAction __instance, AbstractPower ___powerInstance) {
            if (___powerInstance != null && __instance.target != null && ___powerInstance.type == AbstractPower.PowerType.DEBUFF && __instance.target.hasPower(EMPPower.POWER_ID) && !(___powerInstance instanceof EMPPower)) {
                __instance.target.getPower(EMPPower.POWER_ID).flash();
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
