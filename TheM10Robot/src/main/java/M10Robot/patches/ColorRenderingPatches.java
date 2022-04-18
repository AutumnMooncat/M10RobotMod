//package M10Robot.patches;
//
//import M10Robot.cards.abstractCards.AbstractReloadableCard;
//import com.badlogic.gdx.graphics.Color;
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.helpers.FontHelper;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//public class ColorRenderingPatches {
//    //public static final Color boosterColor = Color.valueOf("65ada1");
//    //public static final Color boosterUpgradeColor = Color.valueOf("c26ad4");
//    //private static final String augmentedKeyword = M10RobotMod.makeID("Augmented");
//
//    @SpirePatch2(clz = AbstractCard.class, method = "renderTitle")
//    public static class BeDifferentColorPls {
//        @SpireInstrumentPatch
//        public static ExprEditor patch() {
//            return new ExprEditor() {
//                @Override
//                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
//                public void edit(MethodCall m) throws CannotCompileException {
//                    //If the method is from the class AnimationState and the method is called update
//                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderRotatedText")) {
//                        m.replace("{" +
//                                //"if(M10Robot.patches.BoosterFieldPatch.hasBoosterEquipped(this)) {" +
//                                //$1 refers to the first input parameter of the method, in this case the float that Gdx.graphics.getDeltaTime() returns
//                                //"$3 = M10Robot.patches.BoosterFieldPatch.getBoosterPrefixes(this) + $3 + M10Robot.patches.BoosterFieldPatch.getBoosterSuffixes(this);" +
//                                "$10 = M10Robot.patches.ColorRenderingPatches.getCardNameColor(this, $10);" +
//                                //"}" +
//                                //Call the method as normal
//                                "$proceed($$);" +
//                                "}");
//                    }
//                }
//            };
//        }
//    }
//
//    @SpirePatch2(clz = AbstractCard.class, method = "renderEnergy")
//    public static class BeDifferentColorPls2 {
//        @SpireInstrumentPatch
//        public static ExprEditor patch() {
//            return new ExprEditor() {
//                @Override
//                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
//                public void edit(MethodCall m) throws CannotCompileException {
//                    //If the method is from the class AnimationState and the method is called update
//                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderRotatedText")) {
//                        m.replace("{" +
//                                //"if(M10Robot.patches.BoosterFieldPatch.hasBoosterEquipped(this)) {" +
//                                //$1 refers to the first input parameter of the method, in this case the float that Gdx.graphics.getDeltaTime() returns
//                                //"$3 = M10Robot.patches.BoosterFieldPatch.getBoosterPrefixes(this) + $3 + M10Robot.patches.BoosterFieldPatch.getBoosterSuffixes(this);" +
//                                "$10 = M10Robot.patches.ColorRenderingPatches.getCardEnergyColor(this, $10);" +
//                                //"}" +
//                                //Call the method as normal
//                                "$proceed($$);" +
//                                "}");
//                    }
//                }
//            };
//        }
//    }
//
//    public static Color getCardNameColor(AbstractCard c, Color originalColor) {
//        if (c instanceof AbstractReloadableCard && ((AbstractReloadableCard) c).needsReload) {
//            return AbstractReloadableCard.RELOADING_RED;
//        }
//        return originalColor;
//    }
//
//    public static Color getCardEnergyColor(AbstractCard c, Color originalColor) {
//        if (c instanceof AbstractReloadableCard && ((AbstractReloadableCard) c).needsReload) {
//            return AbstractReloadableCard.RELOADING_RED;
//        }
//        return originalColor;
//    }
//}
