package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FindAndReplaceCardAction extends AbstractGameAction{


    private final AbstractCard toReplace;
    private final AbstractCard newCard;

    public FindAndReplaceCardAction(AbstractCard toReplace, AbstractCard newCard) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.toReplace = toReplace;
        this.newCard = newCard;
    }

    public void update() {
        int index = 0;
        boolean found = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group)
        {
            if (card.cardID.equals(toReplace.cardID)) {
                found = true;
                break;
            }
            index++;
        }
        if(found) {
            this.addToTop(new TransformCardInHandAction(index, newCard));
        }
        this.isDone = true;
    }
}