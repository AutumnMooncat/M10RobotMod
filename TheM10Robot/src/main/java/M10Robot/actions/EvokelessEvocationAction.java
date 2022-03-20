package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

public class EvokelessEvocationAction extends AbstractGameAction {
    private AbstractOrb orb;

    public EvokelessEvocationAction(AbstractOrb orb) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.orb = orb;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && this.orb != null && !(orb instanceof EmptyOrbSlot)) {
            orb.onEvoke();
        }
        this.tickDuration();
    }
}
