package M10Robot.patches;

import M10Robot.powers.interfaces.BonusDamagePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class BonusDamagePowerPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class ModifyBeforeBlock {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void modify(AbstractCreature __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractPower p : __instance.powers) {
                if (p instanceof BonusDamagePower) {
                    damageAmount[0] = ((BonusDamagePower) p).modifyDamageBeforeBlock(__instance, info, damageAmount[0]);
                }
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "decrementBlock");
            return LineFinder.findAllInOrder(ctBehavior, matcher);
        }
    }
}
