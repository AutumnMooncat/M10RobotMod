package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.util.OverclockUtil;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public abstract class AbstractCustomOrb extends AbstractOrb {

    private static final String UPGRADE_ID = M10RobotMod.makeID("UpgradeText");
    private static final OrbStrings ORB_STRINGS = CardCrawlGame.languagePack.getOrbString(UPGRADE_ID);
    public static final String[] UPGRADE_TEXT = ORB_STRINGS.DESCRIPTION;

    public int timesUpgraded;
    private String baseName;
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

    public AbstractCustomOrb(String ID, String baseName, int passiveAmount, int evokeAmount) {
        this(ID, baseName, passiveAmount, evokeAmount, 0);
    }

    public AbstractCustomOrb(String ID, String baseName, int passiveAmount, int evokeAmount, int timesUpgraded) {
        this(ID, baseName, passiveAmount, evokeAmount, timesUpgraded, null, null, null, null, null, null);
    }

    public AbstractCustomOrb(String ID, String baseName, int passiveAmount, int evokeAmount, Texture idleImage, Texture attackImage, Texture hurtImage, Texture successImage, Texture failureImage, Texture throwImage) {
        this(ID, baseName, passiveAmount, evokeAmount, 0, idleImage, attackImage, hurtImage, successImage, failureImage, throwImage);
    }

    public AbstractCustomOrb(String ID, String baseName, int passiveAmount, int evokeAmount, int timesUpgraded, Texture idleImage, Texture attackImage, Texture hurtImage, Texture successImage, Texture failureImage, Texture throwImage) {
        this.ID = ID;
        this.baseName = this.name = baseName;
        this.passiveAmount = this.basePassiveAmount = passiveAmount;
        this.evokeAmount = this.baseEvokeAmount = evokeAmount;

        this.img = idleImage;
        this.idleImage = idleImage;
        this.attackImage = attackImage;
        this.hurtImage = hurtImage;
        this.successImage = successImage;
        this.failureImage = failureImage;
        this.throwImage = throwImage;

        for (int i = 0 ; i < timesUpgraded ; i++) {
            upgrade();
        }
    }

    public abstract void upgrade();

    public boolean canUpgrade() {
        return true;
    }

    protected void upgradeName() {
        timesUpgraded++;
        name = baseName + "+" + timesUpgraded;
    }

    protected void upgradePassive(int amount) {
        basePassiveAmount += amount;
        passiveAmount = basePassiveAmount;
    }

    protected void upgradeEvoke(int amount) {
        baseEvokeAmount += amount;
        evokeAmount = baseEvokeAmount;
    }

/*    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null) {
            clickUpdate();
        }
    }

    private void clickUpdate() {
        if (!AbstractDungeon.isScreenUp && HitboxRightClick.rightClicked.get(this.hb) && !AbstractDungeon.actionManager.turnHasEnded) {
            onRightClick();
        }
    }

    protected void onRightClick() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (OverclockUtil.canOverclock(AbstractCustomOrb.this)) {
                    OverclockUtil.spendComponents(OverclockUtil.getOverclockCost(AbstractCustomOrb.this));
                    AbstractCustomOrb.this.upgrade();
                    playAnimation(successImage, LONG_ANIM);
                }
                this.isDone = true;
            }
        });
    }*/

    protected void applyFocusToPassiveOnly() {
        AbstractPower power = AbstractDungeon.player.getPower("Focus");
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
        } else {
            this.passiveAmount = this.basePassiveAmount;
        }
    }

    protected void applyFocusToEvokeOnly() {
        AbstractPower power = AbstractDungeon.player.getPower("Focus");
        if (power != null) {
            this.evokeAmount = Math.max(0, this.baseEvokeAmount + power.amount);
        } else {
            this.evokeAmount = this.baseEvokeAmount;
        }
    }

    protected void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void onChannel() {}

    public void onLinkedPowerTrigger() {}

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

    public void onPlayCard(AbstractCard card) {}

    public void onAttacked(DamageInfo info) {}

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
