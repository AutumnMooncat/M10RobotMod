//package M10Robot.patches;
//
//import M10Robot.cutStuff.powers.ImmovablePower;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//
//public class ImmovablePatches {
//    @SpirePatch2(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
//    @SpirePatch2(clz = AbstractCreature.class, method = "brokeBlock")
//    public static class noLoseBlock {
//        @SpirePrefixPatch
//        public static SpireReturn<?> plz(AbstractCreature __instance) {
//            if (__instance.hasPower(ImmovablePower.POWER_ID)) {
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//    }
//}
