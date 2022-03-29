package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.actions.OverclockCardAction;
import M10Robot.powers.interfaces.OnExtractPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ExtrapolatePower extends AbstractPower implements CloneablePowerInterface, OnExtractPower {

    public static final String POWER_ID = M10RobotMod.makeID("ExtrapolatePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ExtrapolatePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("attackBurn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

/*    @Override
    public void atStartOfTurn() {
        if (amount > 1) {
            for(int i = 0; i < amount - 1; ++i) {// 46
                this.addToBot(new EvokeWithoutRemovingOrbAction(1));// 47
            }
            this.addToBot(new AnimateOrbAction(1));
            this.addToBot(new EvokeOrbAction(1));
        }
    }*/

    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExtrapolatePower(owner, amount);
    }


    @Override
    public int modifyExtractAmount(int amount) {
        return amount;
    }

    @Override
    public void onExtractCard(AbstractCard c) {
        flash();
        this.addToBot(new OverclockCardAction(c, amount));
    }
}
