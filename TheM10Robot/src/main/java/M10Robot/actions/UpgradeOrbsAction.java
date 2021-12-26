package M10Robot.actions;

import M10Robot.orbs.AbstractCustomOrb;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class UpgradeOrbsAction extends AbstractGameAction {
    private final AbstractOrb orb;

    public UpgradeOrbsAction(int upgrades) {
        this(null, upgrades);
    }

    public UpgradeOrbsAction(AbstractOrb orb, int upgrades) {
        this.orb = orb;
        this.amount = upgrades;
    }

    @Override
    public void update() {
        if (orb == null) {
            for (AbstractOrb o : AbstractDungeon.player.orbs) {
                upgrade(o);
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
        } else {
            ReflectionHacks.setPrivate(o, AbstractOrb.class, "basePassiveAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "basePassiveAmount") + 1);
            ReflectionHacks.setPrivate(o, AbstractOrb.class, "baseEvokeAmount", ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "baseEvokeAmount") + 1);
            o.passiveAmount++;
            o.evokeAmount++;
            o.updateDescription();
        }
    }

}
