package M10Robot.actions;

import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.orbs.OrbUpgradeField;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;

public class UpgradeOrbsAction extends AbstractGameAction {
    private AbstractOrb orb;
    private boolean allOrbs;

    public UpgradeOrbsAction(boolean allOrbs, int upgrades) {
        this.allOrbs = allOrbs;
        this.amount = upgrades;
    }

    public UpgradeOrbsAction(AbstractOrb orb, int upgrades) {
        this.orb = orb;
        this.amount = upgrades;
    }

    @Override
    public void update() {
        if (orb == null) {
            if (allOrbs) {
                for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    upgrade(o);
                }
            } else {
                ArrayList<AbstractOrb> validOrbs = new ArrayList<>();
                for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    if (!(o instanceof EmptyOrbSlot)) {
                        validOrbs.add(o);
                    }
                }
                if (!validOrbs.isEmpty()) {
                    upgrade(validOrbs.get(AbstractDungeon.cardRng.random(validOrbs.size() - 1)));
                }
            }
        } else {
            upgrade(orb);
        }
        this.isDone = true;
    }

    private void upgrade(AbstractOrb o) {
        if (o instanceof AbstractCustomOrb) {
            for (int i = 0 ; i < amount ; i++) {
                ((AbstractCustomOrb) o).upgrade();
            }
            ((AbstractCustomOrb) o).playAnimation(((AbstractCustomOrb) o).successImage, AbstractCustomOrb.LONG_ANIM);
        } else if (!(o instanceof EmptyOrbSlot)) {
            ReflectionHacks.setPrivate(o, AbstractOrb.class, "basePassiveAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "basePassiveAmount") + amount);
            ReflectionHacks.setPrivate(o, AbstractOrb.class, "baseEvokeAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "baseEvokeAmount") + amount);
            o.passiveAmount += amount;
            o.evokeAmount += amount;
            OrbUpgradeField.upgradeCount(o, amount);
            o.updateDescription();
        }
    }

}
