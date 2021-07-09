package M10Robot.powers;

import M10Robot.M10RobotMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RecoilPower extends AbstractPower implements CloneablePowerInterface, BetterOnApplyPowerPower {

    public static final String POWER_ID = M10RobotMod.makeID("RecoilPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color redColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    private final Color greenColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public RecoilPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
        this.priority = 101;
        this.canGoNegative = true;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("noattack");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 0.50F;
        } else {
            return damage;
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            this.addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount > 0) {
            this.redColor.a = c.a;
            c = this.redColor;
        } else if (this.amount < 0 && this.canGoNegative) {
            this.greenColor.a = c.a;
            c = this.greenColor;
        }
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            if (amount == 1) {
                description = DESCRIPTIONS[0];
            } else {
                description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
            }
            this.type = PowerType.DEBUFF;
        } else {
            int tmp = -this.amount;
            if (tmp == 1) {
                description = DESCRIPTIONS[3];
            } else {
                this.description = DESCRIPTIONS[4] + tmp + DESCRIPTIONS[5];
            }
            this.type = PowerType.BUFF;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new RecoilPower(owner, amount);
    }

    @Override
    public boolean betterOnApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof RecoilPower && abstractCreature == owner) {
            int tmp = -this.amount;
            if (tmp >= abstractPower.amount) {
                this.flash();
                this.stackPower(abstractPower.amount);
                if (this.amount == 0) {
                    this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                } else {
                    this.updateDescription();
                }
                return false;
            }
        }
        return true;
    }
}