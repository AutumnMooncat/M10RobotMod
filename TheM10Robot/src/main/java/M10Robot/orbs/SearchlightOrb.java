package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.actions.FasterLoseHPAction;
import M10Robot.powers.SpikesPower;
import M10Robot.relics.SearchlightMk2;
import M10Robot.util.OverclockUtil;
import M10Robot.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import static M10Robot.M10RobotMod.makeOrbPath;

public class SearchlightOrb extends AbstractCustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = M10RobotMod.makeID("SearchlightOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    public static final Texture IDLE_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_00.png"));
    public static final Texture ATTACK_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_01.png"));
    public static final Texture HURT_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_02.png"));
    public static final Texture SUCCESS_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_03.png"));
    public static final Texture FAILURE_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_04.png"));
    public static final Texture THROW_IMG = TextureLoader.getTexture(makeOrbPath("SearchlightOrb_05.png"));
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    private static final int PASSIVE_DAMAGE = 2;
    private static final int EVOKE_AMOUNT = 2;
    private static final int UPGRADE_PLUS_PASSIVE_DAMAGE = 1;
    private static final int UPGRADE_PLUS_EVOKE_AMOUNT = 1;

    public SearchlightOrb() {
        this(0);
    }

    public SearchlightOrb(int timesUpgraded) {
        super(ORB_ID, orbString.NAME, PASSIVE_DAMAGE, EVOKE_AMOUNT, timesUpgraded, IDLE_IMG, ATTACK_IMG, HURT_IMG, SUCCESS_IMG, FAILURE_IMG, THROW_IMG);
        updateDescription();
        channelAnimTimer = 0.5f;
    }

    @Override
    public void upgrade() {
        if (canUpgrade()) {
            upgradeName();
            upgradePassive(UPGRADE_PLUS_PASSIVE_DAMAGE);
            upgradeEvoke(UPGRADE_PLUS_EVOKE_AMOUNT);
            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            updateDescription();
        }
    }

    @Override
    public void onChannel() {
        if (p.hasRelic(SearchlightMk2.ID)) {
            p.getRelic(SearchlightMk2.ID).onTrigger();
            for (int i = 0 ; i < SearchlightMk2.UPGRADES ; i++) {
                upgrade();
            }
        }
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description =
                DESC[0] + passiveAmount + DESC[1] + evokeAmount + DESC[2]/* +
                UPGRADE_TEXT[0] +
                DESC[3] + UPGRADE_PLUS_PASSIVE_DAMAGE + DESC[4] + UPGRADE_PLUS_EVOKE_AMOUNT + DESC[5]*/;
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                playAnimation(ATTACK_IMG, MED_ANIM);
                this.isDone = true;
            }
        });
        this.addToBot(new ApplyPowerAction(p, p, new SpikesPower(p, evokeAmount)));
    }

    @Override
    public void onAttacked(DamageInfo info) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != p) {
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    playAnimation(ATTACK_IMG, MED_ANIM);
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(info.owner.hb.cX, info.owner.hb.cY, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    AbstractDungeon.effectList.add(new OrbFlareEffect(SearchlightOrb.this, OrbFlareEffect.OrbFlareColor.FROST));
                    int damage = passiveAmount;
                    if (info.owner.hasPower(LockOnPower.POWER_ID)) {
                        damage = (int)(damage * 1.5F);
                    }
                    if (!info.owner.isDeadOrEscaped()) {
                        info.owner.damage(new DamageInfo(p, damage, DamageInfo.DamageType.THORNS));
                        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                            AbstractDungeon.actionManager.clearPostCombatActions();
                        }
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();
        //angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(cX, cY)); // This is the purple-sparkles in the orb. You can change this to whatever fits your orb.
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
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
    }

    @Override
    public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new SearchlightOrb(timesUpgraded);
    }

}
