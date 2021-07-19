package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class CheckIfUnblockedAction extends AbstractGameAction {

    private final AbstractCreature source;
    private final AbstractGameAction action;

    public CheckIfUnblockedAction(AbstractCreature target, AbstractCreature source, AbstractGameAction action) {
        this.actionType = ActionType.SPECIAL;
        this.target = target;
        this.source = source;
        this.action = action;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (target.lastDamageTaken > 0) {
            this.addToTop(action);
        }
        this.isDone = true;
    }
}