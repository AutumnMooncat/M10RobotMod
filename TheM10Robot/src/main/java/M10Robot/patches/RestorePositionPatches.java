package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class RestorePositionPatches {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class PositionBackUp {
        public static SpireField<Float> drawX = new SpireField<>(() -> null);
        public static SpireField<Float> drawY = new SpireField<>(() -> null);
        public static SpireField<Float> hbX = new SpireField<>(() -> null);
        public static SpireField<Float> hbY = new SpireField<>(() -> null);
    }

    public static void setBackUp(AbstractPlayer p, float drawX, float drawY, float hbX, float hbY) {
        PositionBackUp.drawX.set(p, drawX);
        PositionBackUp.drawY.set(p, drawY);
        PositionBackUp.hbX.set(p, hbX);
        PositionBackUp.hbY.set(p, hbY);
    }

    public static void removeBackUp(AbstractPlayer p) {
        PositionBackUp.drawX.set(p, null);
        PositionBackUp.drawY.set(p, null);
        PositionBackUp.hbX.set(p, null);
        PositionBackUp.hbY.set(p, null);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class resetPosition {
        @SpirePrefixPatch
        public static void reset(AbstractPlayer __instance) {
            if (PositionBackUp.drawX.get(__instance) != null && PositionBackUp.drawY.get(__instance) != null) {
                __instance.movePosition(PositionBackUp.drawX.get(__instance), PositionBackUp.drawY.get(__instance));
            }
            if (PositionBackUp.hbX.get(__instance) != null && PositionBackUp.hbY.get(__instance) != null) {
                __instance.hb.move(PositionBackUp.hbX.get(__instance), PositionBackUp.hbY.get(__instance));
            }
            removeBackUp(__instance);
        }
    }
}
