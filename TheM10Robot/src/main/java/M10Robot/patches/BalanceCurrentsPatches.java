package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.powers.watcher.WaveOfTheHandPower;

public class BalanceCurrentsPatches {
    public static boolean looping;
    @SpirePatch2(clz = WaveOfTheHandPower.class, method = "onGainedBlock")
    public static class StopLooping {
        @SpirePrefixPatch
        public static SpireReturn<?> plz(WaveOfTheHandPower __instance, float blockAmount) {
            if (looping) {
                looping = false;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
