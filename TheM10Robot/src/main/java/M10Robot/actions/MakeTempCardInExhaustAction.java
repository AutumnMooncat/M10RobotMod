package M10Robot.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class MakeTempCardInExhaustAction extends AbstractGameAction {
    private AbstractCard c;
    private final int numCards;

    public MakeTempCardInExhaustAction(AbstractCard card, int amount) {
        UnlockTracker.markCardAsSeen(card.cardID);
        this.numCards = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = Settings.FAST_MODE ? Settings.ACTION_DUR_FAST : 0.5F;
        this.duration = this.startDuration;
        this.c = card;
    }

    public void update() {
        if (this.duration == this.startDuration) {

            for(int i = 0; i < this.numCards; ++i) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToExhaustEffect(this.makeNewCard()));
            }

            this.duration -= Gdx.graphics.getDeltaTime();
        }

        this.tickDuration();
    }

    private AbstractCard makeNewCard() {
        return this.c.makeStatEquivalentCopy();
    }
}
