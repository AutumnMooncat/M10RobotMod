package M10Robot.patches;

import M10Robot.powers.interfaces.OnBlockDamagePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OnBlockDamagePatches {
    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class OverrideEnergy {
        public static void Prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            boolean fullyBlocked = __instance.currentBlock >= damageAmount;
            for (AbstractPower p : __instance.powers) {
                if (p instanceof OnBlockDamagePower) {
                    if (fullyBlocked) {
                        ((OnBlockDamagePower) p).onFullyBlock(info, damageAmount, __instance.currentBlock);
                    } else {
                        ((OnBlockDamagePower) p).onPartialBlock(info, damageAmount, __instance.currentBlock);
                    }
                }
            }
        }
    }
}
