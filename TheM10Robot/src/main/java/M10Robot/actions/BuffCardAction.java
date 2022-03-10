package M10Robot.actions;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BuffCardAction extends AbstractGameAction {
    public enum BUFF_TYPE{
        DAMAGE,
        BLOCK,
        MAGIC,
        MAGIC2
    }
    public AbstractCard card;
    public BUFF_TYPE type;

    public BuffCardAction(AbstractCard card, BUFF_TYPE type, int amount) {
        this.card = card;
        this.type = type;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        switch (type) {
            case DAMAGE:
                card.baseDamage += amount;
                break;
            case BLOCK:
                card.baseBlock += amount;
                break;
            case MAGIC:
                card.baseMagicNumber += amount;
                break;
            case MAGIC2:
                if (card instanceof AbstractModdedCard) {
                    ((AbstractModdedCard) card).baseSecondMagicNumber += amount;
                }
                break;
        }
        card.applyPowers();
        isDone = true;
    }
}
