package M10Robot.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CtBehavior;

public abstract class AbstractCustomOrb extends AbstractOrb {

    protected AbstractLinkedOrbPower linkedPower;
    protected AbstractPlayer p = AbstractDungeon.player;
    public final Texture idleImage;
    public final Texture attackImage;
    public final Texture hurtImage;
    public final Texture successImage;
    public final Texture failureImage;
    public final Texture throwImage;
    protected float animationTimer;
    public static float SHORT_ANIM = 0.3f;
    public static float MED_ANIM = 0.5f;
    public static float LONG_ANIM = 0.7f;
    protected static final float GENERIC_X_OFFSET = 20f * Settings.scale;

    public AbstractCustomOrb() {
        this(null, null, null, null, null, null);
    }

    public AbstractCustomOrb(Texture idleImage, Texture attackImage, Texture hurtImage, Texture successImage, Texture failureImage, Texture throwImage) {
        this.idleImage = idleImage;
        this.attackImage = attackImage;
        this.hurtImage = hurtImage;
        this.successImage = successImage;
        this.failureImage = failureImage;
        this.throwImage = throwImage;
        this.img = idleImage;
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void onChannel() {
    }

    public void onLinkedPowerTrigger() {
    }

    public void playAnimation(Texture newImage, float time) {
        this.img = newImage;
        this.animationTimer = time;
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        if (animationTimer != -1 && idleImage != null) {
            animationTimer -= Gdx.graphics.getDeltaTime();
            if (animationTimer <= 0) {
                this.img = idleImage;
                this.animationTimer = -1;
            }
        }
    }

    public float getXPosition() {
        return this.hb.cX;
    }

    public float getYPosition() {
        return this.hb.cY + this.bobEffect.y / 2.0F;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    public static class onChannelPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void onChannel(AbstractPlayer __instance, @ByRef AbstractOrb[] orbToSet) {
            if (orbToSet[0] instanceof AbstractCustomOrb) {
                ((AbstractCustomOrb)orbToSet[0]).onChannel();
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractOrb.class, "applyFocus");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
