package M10Robot.powers;

import M10Robot.M10RobotMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RepairableArmorPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("RepairableArmorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public int maxCapacity;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public RepairableArmorPower(AbstractCreature owner, int amount, int maxCapacity) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.maxCapacity = maxCapacity;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("channel");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_PLATED", 0.05F);
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        if (this.amount > 0) {
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (amount > 0) {
            this.reducePower(1);
            this.updateDescription();
            AbstractDungeon.onModifyPower();
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (amount > maxCapacity) {
            amount = maxCapacity;
        }
        updateDescription();
    }

    public void modifyMaxStacks(int delta) {
        this.maxCapacity += delta;
        if (amount > maxCapacity) {
            amount = maxCapacity;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + maxCapacity + DESCRIPTIONS[2];
    }


    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        if (this.amount > 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.maxCapacity), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    @Override
    public AbstractPower makeCopy() {
        return new RepairableArmorPower(owner, amount, maxCapacity);
    }

}
