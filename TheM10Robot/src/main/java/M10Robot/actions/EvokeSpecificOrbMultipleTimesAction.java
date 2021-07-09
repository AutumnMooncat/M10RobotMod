package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class EvokeSpecificOrbMultipleTimesAction extends AbstractGameAction {
    private final AbstractOrb orb;

    public EvokeSpecificOrbMultipleTimesAction(AbstractOrb orb, int times) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.orb = orb;
        this.amount = times;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST && this.orb != null && amount > 0) {
            AbstractDungeon.player.orbs.remove(this.orb);
            AbstractDungeon.player.orbs.add(0, this.orb);
            for (int i = 0 ; i < amount - 1 ; i++) {
                AbstractDungeon.player.evokeWithoutLosingOrb();
            }
            AbstractDungeon.player.evokeOrb();
        }

        this.tickDuration();
    }
}
