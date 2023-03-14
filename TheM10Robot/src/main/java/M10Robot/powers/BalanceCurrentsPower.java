package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.patches.BalanceCurrentsPatches;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BalanceCurrentsPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("BalanceCurrentsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BalanceCurrentsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.loadRegion("controlled_change");

        updateDescription();
    }

    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.type == PowerType.DEBUFF && !power.ID.equals("Shackled") && source == this.owner && !target.hasPower("Artifact")) {
            this.flash();
            BalanceCurrentsPatches.looping = true;
            this.addToBot(new GainBlockAction(owner, owner, amount, true));
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    BalanceCurrentsPatches.looping = false;
                    this.isDone = true;
                }
            });
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BalanceCurrentsPower(owner, amount);
    }
}