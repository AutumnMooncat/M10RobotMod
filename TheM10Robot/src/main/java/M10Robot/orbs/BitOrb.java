package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.actions.BitAttackAction;
import M10Robot.actions.BitEvokeAction;
import M10Robot.powers.WideAnglePower;
import M10Robot.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;

import static M10Robot.M10RobotMod.makeOrbPath;

public class BitOrb extends AbstractCustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = M10RobotMod.makeID("BitOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    public static final Texture IDLE_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_00s.png"));
    public static final Texture ATTACK_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_01s.png"));
    public static final Texture HURT_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_02s.png"));
    public static final Texture SUCCESS_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_03s.png"));
    public static final Texture FAILURE_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_04s.png"));
    public static final Texture THROW_IMG = TextureLoader.getTexture(makeOrbPath("BitOrb_05s.png"));
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    private static final float MOUTH_OFFSET_X = -10f * Settings.scale;
    private static final float MOUTH_OFFSET_Y = -35f * Settings.scale;

    private static final int PASSIVE_DAMAGE = 2;
    private static final int EVOKE_DAMAGE = PASSIVE_DAMAGE * 2;
    private static final int UPGRADE_PLUS_PASSIVE_DAMAGE = 1;
    private static final int LOCK_ON = 2;

    public BitOrb() {
        this(0);
    }

    public BitOrb(int timesUpgraded) {
        super(ORB_ID, orbString.NAME, PASSIVE_DAMAGE, EVOKE_DAMAGE, timesUpgraded, IDLE_IMG, ATTACK_IMG, HURT_IMG, SUCCESS_IMG, FAILURE_IMG, THROW_IMG);
        updateDescription();
        channelAnimTimer = 0.5f;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            upgradeName();
            upgradePassive(UPGRADE_PLUS_PASSIVE_DAMAGE);
            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            updateDescription();
        }
    }

    @Override
    public void applyFocus() {
        applyFocusToPassiveOnly();
        this.evokeAmount = this.passiveAmount * 2;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus();
        description =
                DESC[0] + passiveAmount + DESC[1] + evokeAmount + DESC[2];
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        this.addToTop(new BitEvokeAction(this, p, LOCK_ON));
    }

    @Override
    public void onPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            int hits = 1;
            if (p.hasPower(WideAnglePower.POWER_ID)) {
                hits += p.getPower(WideAnglePower.POWER_ID).amount;
            }
            for (int i = 0 ; i < hits ; i++) {
                this.addToBot(new BitAttackAction(this, p));
            }
        }
    }

    @Override
    public void onAttacked(DamageInfo info) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS) {
            playAnimation(HURT_IMG, MED_ANIM);
        }
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();
        //angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(cX, cY)); // This is the purple-sparkles in the orb. You can change this to whatever fits your orb.
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

    /*protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
    }*/

    @Override
    public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    @Override
    public float getXPosition() {
        return super.getXPosition() + MOUTH_OFFSET_X;
    }

    @Override
    public float getYPosition() {
        return super.getYPosition() + MOUTH_OFFSET_Y;
    }

    @Override
    public AbstractOrb makeCopy() {
        return new BitOrb(this.timesUpgraded);
    }

}
