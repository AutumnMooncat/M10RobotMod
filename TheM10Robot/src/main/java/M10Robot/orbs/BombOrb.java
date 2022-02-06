package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.actions.BombEvokeAction;
import M10Robot.util.TextureLoader;
import M10Robot.vfx.BigExplosionEffect;
import M10Robot.vfx.BurnToAshEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.*;

import static M10Robot.M10RobotMod.makeOrbPath;

public class BombOrb extends AbstractCustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = M10RobotMod.makeID("BombOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    public static final Texture IDLE_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_00.png"));
    public static final Texture ATTACK_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_01.png"));
    public static final Texture HURT_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_02.png"));
    public static final Texture SUCCESS_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_03.png"));
    public static final Texture FAILURE_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_04.png"));
    public static final Texture THROW_IMG = TextureLoader.getTexture(makeOrbPath("BombOrb_05.png"));
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    private static final int PASSIVE_DAMAGE = 50;
    private static final int EVOKE_DAMAGE = 6;
    private static final int UPGRADE_PLUS_PASSIVE_DAMAGE = 25;
    //private static final int UPGRADE_PLUS_EVOKE_DAMAGE = 4;

    private boolean attacked;

    public BombOrb() {
        this(0);
    }

    public BombOrb(int timesUpgraded) {
        super(ORB_ID, orbString.NAME, PASSIVE_DAMAGE, EVOKE_DAMAGE, timesUpgraded, IDLE_IMG, ATTACK_IMG, HURT_IMG, SUCCESS_IMG, FAILURE_IMG, THROW_IMG);
        scale = 1.2f;
        updateDescription();
        channelAnimTimer = 0.5f;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            upgradeName();
            upgradePassive(UPGRADE_PLUS_PASSIVE_DAMAGE);
            //upgradeEvoke(UPGRADE_PLUS_EVOKE_DAMAGE);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocusToPassiveOnly();
        description =
                DESC[0] + passiveAmount + DESC[1] + evokeAmount + DESC[2]/* +
                UPGRADE_TEXT[0] +
                DESC[3] + UPGRADE_PLUS_PASSIVE_DAMAGE + DESC[4]*/;
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        this.addToBot(new BombEvokeAction(p, this));
    }

    @Override
    public void onAttacked(DamageInfo info) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.output > 0) {
            attacked = true;
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    playAnimation(SUCCESS_IMG, MED_ANIM);
                    evokeAmount += Math.max(1, info.output * (passiveAmount / 100F));
                    updateDescription();
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void onStartOfTurn() {
        if (attacked) {
            attacked = false;
        } else {
            playAnimation(FAILURE_IMG, MED_ANIM);
        }
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();
        //angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(cX, cY)); // This is the purple-sparkles in the orb. You can change this to whatever fits your orb.
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    // Render the orb.
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a));
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
        renderText(sb);
        hb.render(sb);
    }

    protected void renderText(SpriteBatch sb) {
        //FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.turnCount), this.cX + NUM_X_OFFSET + (2.5F * GENERIC_X_OFFSET), this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 7.0F * Settings.scale, new Color(1.0F, 0.2F, 0.2F, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, this.passiveAmount + "%", this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
    }


    @Override
    public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new BombOrb(timesUpgraded);
    }

}
