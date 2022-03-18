package M10Robot.actions;

import M10Robot.patches.WasPowerActuallyAppliedPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class DoIfPowerAppliedAction extends AbstractGameAction {
    ApplyPowerAction powerAction;
    AbstractGameAction appliedAction;
    AbstractGameAction negatedAction;

    public DoIfPowerAppliedAction(ApplyPowerAction powerAction, AbstractGameAction appliedAction) {
        this(powerAction, appliedAction, null);
    }

    public DoIfPowerAppliedAction(ApplyPowerAction powerAction, AbstractGameAction appliedAction, AbstractGameAction negatedAction) {
        this.powerAction = powerAction;
        this.appliedAction = appliedAction;
        this.negatedAction = negatedAction;
    }

    @Override
    public void update() {
        if (WasPowerActuallyAppliedPatches.AppliedField.actuallyApplied.get(powerAction)) {
            if (appliedAction != null) {
                this.addToTop(appliedAction);
            }
        } else {
            if (negatedAction != null) {
                this.addToTop(negatedAction);
            }
        }
        this.isDone = true;
    }
}
