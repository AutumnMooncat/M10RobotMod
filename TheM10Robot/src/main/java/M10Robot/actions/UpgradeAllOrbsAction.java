package M10Robot.actions;

import M10Robot.orbs.AbstractCustomOrb;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class UpgradeAllOrbsAction extends AbstractGameAction {

    public UpgradeAllOrbsAction(int upgrades) {
        this.amount = upgrades;
    }

    @Override
    public void update() {
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof AbstractCustomOrb) {
                for (int i = 0 ; i < amount ; i++) {
                    ((AbstractCustomOrb) o).upgrade();
                }
            } else {
                ReflectionHacks.setPrivate(o, AbstractOrb.class, "basePassiveAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "basePassiveAmount") + 1);
                ReflectionHacks.setPrivate(o, AbstractOrb.class, "baseEvokeAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "baseEvokeAmount") + 1);
                o.passiveAmount++;
                o.evokeAmount++;
                o.updateDescription();
            }
        }
        this.isDone = true;
    }
}
