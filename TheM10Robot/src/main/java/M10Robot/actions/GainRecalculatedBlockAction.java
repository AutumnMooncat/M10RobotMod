package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GainRecalculatedBlockAction extends AbstractGameAction {
    AbstractCard card;

    public GainRecalculatedBlockAction(AbstractCard card) {
        this.card = card;
        this.target = AbstractDungeon.player;
    }

    @Override
    public void update() {
        card.applyPowers();
        this.addToTop(new GainBlockAction(target, card.block));
        this.isDone = true;
    }
}
