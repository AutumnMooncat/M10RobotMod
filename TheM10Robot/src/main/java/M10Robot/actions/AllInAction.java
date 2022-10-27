package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class AllInAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final boolean free;

    public AllInAction(AbstractPlayer p, boolean free, int energyOnUse) {
        this.p = p;
        this.amount = energyOnUse;
        this.free = free;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (p.hasRelic("Chemical X")) {
            this.amount += ChemicalX.BOOST;
            p.getRelic("Chemical X").flash();
        }

        if (this.amount > 0) {
            this.addToBot(new OverclockCardAction(true, this.amount));
        }

        if (!this.free) {
            p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
