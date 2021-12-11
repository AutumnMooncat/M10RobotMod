package M10Robot.actions;

import M10Robot.cardModifiers.OverclockModifier;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class OverclockCardAction extends AbstractGameAction {
    private AbstractCard card;

    public OverclockCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.1F);
        CardModifierManager.addModifier(card, new OverclockModifier());
        card.superFlash();
        if (card instanceof AbstractSwappableCard) {
            CardModifierManager.addModifier(card.cardsToPreview, new OverclockModifier());
        }
        this.isDone = true;
    }
}
