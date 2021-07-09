package M10Robot.orbs;

import M10Robot.M10RobotMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractLinkedOrbPower extends AbstractPower implements CloneablePowerInterface, InvisiblePower, NonStackablePower {

    protected AbstractCustomOrb linkedOrb;

    public AbstractLinkedOrbPower(AbstractCustomOrb linkedOrb) {
        this.linkedOrb = linkedOrb;
        this.owner = AbstractDungeon.player;
        this.name = "";
        this.ID = M10RobotMod.makeID("EmptyPower");
        //this.loadRegion("confusion");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "";
    }

    private void removeMe() {
        this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.player.orbs.contains(linkedOrb)) {
            removeMe();
        }
    }
}
