package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.powers.interfaces.OnBlockDamagePower;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

public class RewindPower extends AbstractPower implements CloneablePowerInterface, OnBlockDamagePower {

    public static final String POWER_ID = M10RobotMod.makeID("RewindPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color whyCantWeJustUseGreenColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    int count;

    public RewindPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("backAttack2");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        count = 0;
    }

    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        this.whyCantWeJustUseGreenColor.a = c.a;
        c = this.whyCantWeJustUseGreenColor;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount-count), x, y, this.fontScale, c);
    }

    @Override
    public AbstractPower makeCopy() {
        return new RewindPower(owner, amount);
    }

    @Override
    public void onPartialBlock(DamageInfo info, int initialDamageAmount, int initialBlock) {
        if (count < amount) {
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    int delta = initialBlock - owner.currentBlock;
                    if (delta > 0 && count < RewindPower.this.amount) {
                        flash();
                        count++;
                        this.addToTop(new ApplyPowerAction(owner, owner, new NextTurnBlockPower(owner, delta)));
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void onFullyBlock(DamageInfo info, int initialDamageAmount, int initialBlock) {
        onPartialBlock(info, initialDamageAmount, initialBlock);
    }
}