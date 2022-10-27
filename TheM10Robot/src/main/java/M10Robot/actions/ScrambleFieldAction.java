package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;

public class ScrambleFieldAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final boolean free;

    public ScrambleFieldAction(AbstractPlayer p, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.amount = energyOnUse;
        this.free = freeToPlayOnce;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (p.hasRelic("Chemical X")) {
            this.amount += ChemicalX.BOOST;
            p.getRelic("Chemical X").flash();
        }

        if (this.amount > 0) {
            this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
            this.addToBot(new VFXAction(p, new IntimidateEffect(p.hb.cX, p.hb.cY), 0.7F));
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.amount)));
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(aM, p, new StrengthPower(aM, -this.amount), -this.amount));
                }
            }
        }

        if (!this.free) {
            p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
