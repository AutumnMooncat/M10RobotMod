package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class MultichannelAction extends AbstractGameAction {
    private final AbstractOrb orb;
    public MultichannelAction(AbstractOrb o, int amount) {
        this.amount = amount;
        this.orb = o;
    }

    @Override
    public void update() {
        for (int i = 0 ; i < amount ; i++) {
            this.addToTop(new ChannelAction(orb.makeCopy()));
        }
        this.isDone = true;
    }
}
