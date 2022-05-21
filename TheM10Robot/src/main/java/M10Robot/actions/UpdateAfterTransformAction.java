package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UpdateAfterTransformAction extends AbstractGameAction {
    private final AbstractCard cardToUpdate;

    public UpdateAfterTransformAction(AbstractCard cardToUpdate) {
        this.cardToUpdate = cardToUpdate;
    }

    @Override
    public void update() {
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.player.hand.glowCheck();
        cardToUpdate.superFlash();
        cardToUpdate.initializeDescription();
        this.isDone = true;
    }
}
