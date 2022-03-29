//package M10Robot.patches;
//
//import M10Robot.cutStuff.powers.EMPPower;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
//import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.powers.GainStrengthPower;
//
//public class EMPPatches {
//    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
//    public static class DontRemovePower {
//        @SpirePrefixPatch
//        public static SpireReturn<?> dontRemove(RemoveSpecificPowerAction __instance, AbstractPower ___powerInstance, String ___powerToRemove) {
//            if (__instance.target != null && __instance.target.hasPower(EMPPower.POWER_ID)) {
//                if (___powerInstance != null && ___powerInstance.type == AbstractPower.PowerType.DEBUFF  && !(___powerInstance instanceof EMPPower) && !(___powerInstance instanceof GainStrengthPower) && !(___powerInstance.canGoNegative && ___powerInstance.amount == 0)) {
//                    __instance.target.getPower(EMPPower.POWER_ID).flash();
//                    __instance.isDone = true;
//                    return SpireReturn.Return();
//                } else if (___powerToRemove != null){
//                    AbstractPower p = __instance.target.getPower(___powerToRemove);
//                    if (p != null && p.type == AbstractPower.PowerType.DEBUFF && !(p instanceof EMPPower) && !(p.canGoNegative && p.amount == 0)) {
//                        __instance.target.getPower(EMPPower.POWER_ID).flash();
//                        __instance.isDone = true;
//                        return SpireReturn.Return();
//                    }
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }
//
//    @SpirePatch(clz = ReducePowerAction.class, method = "update")
//    public static class DontReducePower {
//        @SpirePrefixPatch
//        public static SpireReturn<?> dontReduce(ReducePowerAction __instance, AbstractPower ___powerInstance, String ___powerID) {
//            if (__instance.target != null && __instance.target.hasPower(EMPPower.POWER_ID)) {
//                if (___powerInstance != null && ___powerInstance.type == AbstractPower.PowerType.DEBUFF  && !(___powerInstance instanceof EMPPower) && !(___powerInstance instanceof GainStrengthPower)) {
//                    __instance.target.getPower(EMPPower.POWER_ID).flash();
//                    __instance.isDone = true;
//                    return SpireReturn.Return();
//                } else if (___powerID != null){
//                    AbstractPower p = __instance.target.getPower(___powerID);
//                    if (p != null && p.type == AbstractPower.PowerType.DEBUFF && !(p instanceof EMPPower)) {
//                        __instance.target.getPower(EMPPower.POWER_ID).flash();
//                        __instance.isDone = true;
//                        return SpireReturn.Return();
//                    }
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }
//}
