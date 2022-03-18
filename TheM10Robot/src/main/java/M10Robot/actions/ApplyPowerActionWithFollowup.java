package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class ApplyPowerActionWithFollowup extends AbstractGameAction {
    ApplyPowerAction powerAction;
    AbstractGameAction appliedAction;
    AbstractGameAction negatedAction;

    public ApplyPowerActionWithFollowup(ApplyPowerAction powerAction, AbstractGameAction appliedAction) {
        this(powerAction, appliedAction, null);
    }

    public ApplyPowerActionWithFollowup(ApplyPowerAction powerAction, AbstractGameAction appliedAction, AbstractGameAction negatedAction) {
        this.powerAction = powerAction;
        this.appliedAction = appliedAction;
        this.negatedAction = negatedAction;
    }

    @Override
    public void update() {
        this.addToTop(new DoIfPowerAppliedAction(powerAction, appliedAction, negatedAction));
        this.addToTop(powerAction);
        this.isDone = true;
    }

}
