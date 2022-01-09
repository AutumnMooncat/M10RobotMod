package M10Robot.patches;

import M10Robot.powers.interfaces.OnBlockDamagePower;
import M10Robot.relics.RoboBall;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OnBlockDamagedPatches {
    @SpirePatch(clz = AbstractCreature.class, method = "decrementBlock")
    public static class BlockPatch {
        public static SpireReturn<?> Prefix(AbstractCreature __instance, DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && __instance == AbstractDungeon.player && AbstractDungeon.player.hasRelic(RoboBall.ID) && !AbstractDungeon.player.getRelic(RoboBall.ID).grayscale) {
                return SpireReturn.Return(damageAmount);
            }
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
            return SpireReturn.Continue();
        }
    }
}
