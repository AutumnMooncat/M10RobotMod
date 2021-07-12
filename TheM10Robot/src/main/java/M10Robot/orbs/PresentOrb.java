package M10Robot.orbs;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.util.TextureLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static M10Robot.M10RobotMod.makeOrbPath;
import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_HEIGHT;

public class PresentOrb extends AbstractCustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = M10RobotMod.makeID("PresentOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;
    public static final int CHARGES_PER_BOOSTER = 10;

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

    int currentAmount;
    private static final ArrayList<AbstractCard> cards = new ArrayList<>();
    private AbstractCard cardsToPreview;
    private static final float drawScaleFactor = 0.85F;

    static {
        cards.addAll(AbstractDungeon.commonCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
        cards.addAll(AbstractDungeon.uncommonCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
        cards.addAll(AbstractDungeon.rareCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
    }

    public PresentOrb() {

        super(IDLE_IMG, ATTACK_IMG, HURT_IMG, SUCCESS_IMG, FAILURE_IMG, THROW_IMG);
        ID = ORB_ID;
        name = orbString.NAME;

        linkedPower = new PresentOrbPower(this);

        evokeAmount = baseEvokeAmount = 2;
        passiveAmount = basePassiveAmount = 4;
        currentAmount = 0;
        chooseNewBooster();

        updateDescription();

        //angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;

    }

    @Override
    public void onChannel() {
        this.addToBot(new ApplyPowerAction(p, p, linkedPower));
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description = DESC[0] + (int)(passiveAmount*100f/CHARGES_PER_BOOSTER) + DESC[1] + cardsToPreview + DESC[2] + DESC[3] + cardsToPreview + DESC[4];
    }

    public void applyFocus() {
        AbstractPower power = AbstractDungeon.player.getPower("Focus");
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
        } else {
            this.passiveAmount = this.basePassiveAmount;
        }
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        createBooster();
        this.addToTop(new RemoveSpecificPowerAction(p, p, linkedPower));
    }

    private void createBooster() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                playAnimation(SUCCESS_IMG, MED_ANIM);
                this.isDone = true;
            }
        });
        this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy()));
        chooseNewBooster();
    }

    private void chooseNewBooster() {
        cardsToPreview = cards.get(AbstractDungeon.cardRandomRng.random(cards.size()-1));
    }

    @Override
    public void onStartOfTurn() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                playAnimation(THROW_IMG, SHORT_ANIM);
                this.isDone = true;
            }
        });
        this.currentAmount += passiveAmount;
        while (currentAmount >= CHARGES_PER_BOOSTER) {
            createBooster();
            currentAmount -= CHARGES_PER_BOOSTER;
            updateDescription();
        }
        updateDescription();
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
        renderCardPreview(sb);
    }

    public void renderCardPreview(SpriteBatch sb) {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard && cardsToPreview != null && this.hb.hovered) {
            float tmpScale = drawScaleFactor * 0.8F;
            /*if (this.hb.cX > (float)Settings.WIDTH * 0.75F) {
                this.cardsToPreview.current_x = this.hb.cX + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * drawScaleFactor;
            } else {
                this.cardsToPreview.current_x = this.hb.cX - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * drawScaleFactor;
            }*/

            this.cardsToPreview.current_x = this.hb.cX;
            this.cardsToPreview.current_y = this.hb.cY + (IMG_HEIGHT / 2.0F) * drawScaleFactor;
            this.cardsToPreview.drawScale = tmpScale;
            this.cardsToPreview.render(sb);
        }
    }

    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, (int)(this.currentAmount*100f/CHARGES_PER_BOOSTER)+"%", this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, "+"+(int)(this.passiveAmount*100f/CHARGES_PER_BOOSTER)+"%", this.cX + NUM_X_OFFSET + GENERIC_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
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
        return new PresentOrb();
    }

    private static class PresentOrbPower extends AbstractLinkedOrbPower {

        public PresentOrbPower(AbstractCustomOrb linkedOrb) {
            super(linkedOrb);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS) {
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        linkedOrb.playAnimation(HURT_IMG, MED_ANIM);
                        this.isDone = true;
                    }
                });
            }
            return damageAmount;
        }

        @Override
        public AbstractPower makeCopy() {
            return new PresentOrbPower(linkedOrb);
        }
    }
}
