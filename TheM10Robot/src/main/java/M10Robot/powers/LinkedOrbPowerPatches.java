package M10Robot.powers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;

public class LinkedOrbPowerPatches {

    @SpirePatch(clz = PowerExpireTextEffect.class, method = "update")
    public static class DontUpdate {
        @SpirePrefixPatch
        public static SpireReturn<?> dontUpdate(PowerExpireTextEffect __instance, String ___msg, TextureAtlas.AtlasRegion ___region) {
            if (___msg.equals("") && ___region == null) {
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = PowerExpireTextEffect.class, method = "render")
    public static class DontRender {
        @SpirePrefixPatch
        public static SpireReturn<?> dontRender(PowerExpireTextEffect __instance, SpriteBatch sb, String ___msg, TextureAtlas.AtlasRegion ___region) {
            if (___msg.equals("") && ___region == null) {
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

}
