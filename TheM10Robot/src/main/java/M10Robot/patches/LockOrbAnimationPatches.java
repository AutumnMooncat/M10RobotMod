package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

public class LockOrbAnimationPatches {

    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class StopAnimatingField {
        public static SpireField<Boolean> stopAnimating = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(clz = AbstractOrb.class, method = "updateAnimation")
    public static class StopTryingToMovePatch {
        @SpirePrefixPatch
        public static SpireReturn<?> stop(AbstractOrb __instance) {
            if (StopAnimatingField.stopAnimating.get(__instance)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
