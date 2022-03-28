package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;

import static M10Robot.M10RobotMod.makeOrbPath;

public class PresentOrb extends AbstractCustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = M10RobotMod.makeID("PresentOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    public static final Texture IDLE_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_00.png"));
    public static final Texture ATTACK_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_01.png"));
    public static final Texture HURT_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_02.png"));
    public static final Texture SUCCESS_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_03.png"));
    public static final Texture FAILURE_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_04.png"));
    public static final Texture THROW_IMG = TextureLoader.getTexture(makeOrbPath("PresentOrb_05.png"));
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    private static final int PASSIVE_AMOUNT = 1;
    private static final int EVOKE_AMOUNT = 2;
    private static final int UPGRADE_PLUS_PASSIVE_AMOUNT= 1;
    private static final int UPGRADE_PLUS_EVOKE_AMOUNT = 1;

    public PresentOrb() {
        this(0);
    }

    public PresentOrb(int timesUpgraded) {
        super(ORB_ID, orbString.NAME, PASSIVE_AMOUNT, EVOKE_AMOUNT, timesUpgraded, IDLE_IMG, ATTACK_IMG, HURT_IMG, SUCCESS_IMG, FAILURE_IMG, THROW_IMG);
        updateDescription();
        channelAnimTimer = 0.5f;

    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            upgradeName(); //Cost will increase once we call upgradeName and increment timesUpgraded
            //upgradePassive(UPGRADE_PLUS_PASSIVE_AMOUNT);
            upgradeEvoke(UPGRADE_PLUS_EVOKE_AMOUNT);
            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            updateDescription();
        }
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocusToEvokeOnly(); // Apply Focus (Look at the next method)
        description =
                DESC[0] + evokeAmount + DESC[1]/* +
                UPGRADE_TEXT[0] +
                DESC[3] + UPGRADE_PLUS_PASSIVE_AMOUNT + DESC[4] + UPGRADE_PLUS_EVOKE_AMOUNT + DESC[5]*/;
    }

    @Override
    public void onStartOfTurn() {
        playAnimation(SUCCESS_IMG, MED_ANIM);
        this.addToBot(new DrawCardAction(PASSIVE_AMOUNT)); //hardcoded so if something modifies passive amount the description is still accurate
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        playAnimation(SUCCESS_IMG, MED_ANIM);
        this.addToTop(new DrawCardAction(evokeAmount));
    }

/*    @Override
    public void onPlayCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            playAnimation(ATTACK_IMG, SHORT_ANIM);
            this.addToBot(new GainBlockAction(p, p, passiveAmount, true));
        }
    }*/

    @Override
    public void onAttacked(DamageInfo info) {
        playAnimation(HURT_IMG, MED_ANIM);
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();
        //angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(cX, cY)); // This is the purple-sparkles in the orb. You can change this to whatever fits your orb.
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

    @Override
    protected void renderText(SpriteBatch sb) {
        if (this.showEvokeValue) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
        }
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
        return new PresentOrb(timesUpgraded);
    }

}
