package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SwapPilesAction extends AbstractGameAction {

    public SwapPilesAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            CardGroup discardTemp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            CardGroup drawTemp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            discardTemp.group.addAll(AbstractDungeon.player.discardPile.group);
            drawTemp.group.addAll(AbstractDungeon.player.drawPile.group);

            for (AbstractCard c : discardTemp.group) {
                AbstractDungeon.getCurrRoom().souls.onToDeck(c, false);
                AbstractDungeon.player.discardPile.removeCard(c);
            }

            for (AbstractCard c : drawTemp.group) {
                AbstractDungeon.getCurrRoom().souls.discard(c);
                AbstractDungeon.player.drawPile.removeCard(c);
            }
        }
        tickDuration();
    }
}
