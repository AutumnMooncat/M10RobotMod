package M10Robot.cutStuff.actions;

import M10Robot.cutStuff.powers.RepairableArmorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class RepairArmorAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;

    public RepairArmorAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
        this.actionType = AbstractGameAction.ActionType.POWER;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && this.target != null) {
            if (target.hasPower(RepairableArmorPower.POWER_ID)) {
                target.getPower(RepairableArmorPower.POWER_ID).flash();
                //((RepairableArmorPower)target.getPower(RepairableArmorPower.POWER_ID)).modifyCurrentAmount(amount);
            }
        }
        this.tickDuration();
    }
}