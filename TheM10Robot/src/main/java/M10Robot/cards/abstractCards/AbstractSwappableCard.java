package M10Robot.cards.abstractCards;

import M10Robot.actions.SwapCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public abstract class AbstractSwappableCard extends AbstractClickableCard {
    private AbstractGameAction action;
    private static boolean looping;

    public AbstractSwappableCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    protected void setLinkedCard(AbstractSwappableCard linkedCard) {
        this.cardsToPreview = linkedCard;
        this.cardsToPreview.cardsToPreview = this;
    }

    @Override
    public void upgrade() {
        if (cardsToPreview != null && !looping) {
            looping = true;
            cardsToPreview.upgrade();
            looping = false;
        }
    }

    @Override
    public void onRightClick() {
        if (canSwap() && action == null && cardsToPreview != null) {
            CardCrawlGame.sound.play("CARD_SELECT", 0.1F);
            action = new SwapCardsAction(this, cardsToPreview);
            this.addToTop(action);
        }
    }

    @Override
    public void update() {
        super.update();
        if (action != null && action.isDone) {
            action = null;
        }
    }

    public boolean canSwap() {
        return true;
    }

    public void onSwapOut() {}

    public void onSwapIn() {}
}
