package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.util.interfaces.OverclockBeforePlayItem;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FollowupPower extends TwoAmountPower implements CloneablePowerInterface, NonStackablePower, OverclockBeforePlayItem {

    public static final String POWER_ID = M10RobotMod.makeID("FollowupPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public FollowupPower(AbstractCreature owner, int cards, int overclocks) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = cards;
        this.amount2 = overclocks;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("burst");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

/*    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0 && OverclockUtil.canOverclock(card)) {
            this.flash();
            this.addToBot(new OverclockCardAction(card, amount2));
            --this.amount;
            if (this.amount == 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                updateDescription();
            }
        }

    }*/

    public void updateDescription() {
        if (amount == 1) {
            if (amount2 == 1) {
                description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1];
            } else {
                description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[2];
            }
        } else {
            if (amount2 == 1) {
                description = DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + amount2 + DESCRIPTIONS[1];
            } else {
                description = DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + amount2 + DESCRIPTIONS[2];
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FollowupPower(owner, amount, amount2);
    }

    @Override
    public int overclockAmount(AbstractCard card) {
        if (amount > 0) {
            return amount2;
        }
        return 0;
    }

    @Override
    public void onOverclock(AbstractCard card) {
        flash();
        --this.amount;
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            if (this.amount < 0) {
                this.amount = 0;
            }
            updateDescription();
        }
    }
}