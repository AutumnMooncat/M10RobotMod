package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DecreaseMaxHPAction extends AbstractGameAction {

    public DecreaseMaxHPAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void update() {
        target.decreaseMaxHealth(amount);
        this.isDone = true;
    }
}
