package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class DynamicNameModificationPatches {
    @SpirePatch2(clz = AbstractCard.class, method = "renderTitle")
    public static class BeDifferentColorPls {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
                public void edit(MethodCall m) throws CannotCompileException {
                    //If the method is from the class AnimationState and the method is called update
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderRotatedText")) {
                        m.replace("{" +
                                //This is usable and refers to the class you#re patching, can be substitued by $0 but that has extra rules
                                "$3 = M10Robot.util.OverclockUtil.getOverclockPrefix(this) + $3 + M10Robot.util.OverclockUtil.getOverclockSuffix(this);" +
                                //"if(M10Robot.patches.BoosterFieldPatch.hasBoosterEquipped(this)) {" +
                                //$1 refers to the first input parameter of the method, in this case the float that Gdx.graphics.getDeltaTime() returns
                                //"$3 = M10Robot.patches.BoosterFieldPatch.getBoosterPrefixes(this) + $3 + M10Robot.patches.BoosterFieldPatch.getBoosterSuffixes(this);" +
                                //"$10 = M10Robot.patches.BoosterFieldPatch.getCardTitleBoosterColor(this);" +
                                //"}" +
                                //Call the method as normal
                                "$proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }
}
