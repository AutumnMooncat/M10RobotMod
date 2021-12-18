//package M10Robot.cutStuff.patches;
//
//import M10Robot.cutStuff.stances.OnLoseHPStance;
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.cards.DamageInfo;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import javassist.CtBehavior;
//
//public class OnLoseHPLastPatches {
//    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
//    public static class DamageTakenListener {
//        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
//        public static void damageTakenListener(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
//            if (__instance.stance instanceof OnLoseHPStance) {
//                damageAmount[0] = ((OnLoseHPStance) __instance.stance).onLoseHP(info, damageAmount[0]);
//            }
//        }
//        private static class Locator extends SpireInsertLocator {
//            @Override
//            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
//                Matcher finalMatcher = new Matcher.MethodCallMatcher(Math.class, "min");
//                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
//            }
//        }
//    }
//}
