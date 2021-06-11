package M10Robot.actions;

import M10Robot.powers.RepairableArmorPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
                this.addToTop(new ApplyPowerAction(target, target, new RepairableArmorPower(target, amount, amount), amount, true));
            }
        }
        this.tickDuration();
    }
}
