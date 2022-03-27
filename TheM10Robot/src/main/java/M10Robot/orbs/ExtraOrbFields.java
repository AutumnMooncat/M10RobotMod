package M10Robot.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ExtraOrbFields {
    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class ExtraFields {
        public static SpireField<Integer> timesUpgraded = new SpireField<>(() -> 0);
        public static SpireField<Integer> passiveIncrease = new SpireField<>(() -> -1);
        public static SpireField<Integer> evokeIncrease = new SpireField<>(() -> -1);
    }

    public static void upgradeCount(AbstractOrb o) {
        upgradeCount(o, 1);
    }

    public static void upgradeCount(AbstractOrb o, int times) {
        ExtraFields.timesUpgraded.set(o, ExtraFields.timesUpgraded.get(o)+times);
    }

    public static String makeUpgradeSuffix(AbstractOrb o, String name) {
        int upgrades;
        if (o instanceof AbstractCustomOrb) {
            upgrades = ((AbstractCustomOrb) o).timesUpgraded;
        } else {
            upgrades = ExtraFields.timesUpgraded.get(o);
        }
        if (upgrades > 0) {
            return name+"+"+upgrades;
        }
        return name;
    }

    @SpirePatch2(clz = AbstractOrb.class, method = "update")
    public static class RenderWithUpgrades {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(TipHelper.class.getName()) && m.getMethodName().equals("renderGenericTip")) {
                        m.replace("$3 = M10Robot.orbs.ExtraOrbFields.makeUpgradeSuffix(this, $3); $proceed($$);");
                    }
                }
            };
        }
    }
}
