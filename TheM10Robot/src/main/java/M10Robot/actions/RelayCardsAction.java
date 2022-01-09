package M10Robot.actions;

import M10Robot.cutStuff.powers.RelayPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RelayCardsAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final List<AbstractCard> cards = new ArrayList<>();
    private final AbstractPlayer p;

    public RelayCardsAction(AbstractCard card) {
        this(new ArrayList<>(Collections.singletonList(card)));
    }

    public RelayCardsAction(List<AbstractCard> cards) {
        this.cards.addAll(cards);
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION) {
            if (p.hasPower(RelayPower.POWER_ID)) {
                RelayPower r = (RelayPower) p.getPower(RelayPower.POWER_ID);
                for (AbstractCard c : cards) {
                    r.relayCards.addToTop(c);
                    //p.hand.empower(c);
                }
                r.amount = r.relayCards.size();
                r.flash();
                r.updateDescription();
            } else {
                for (AbstractCard c : cards) {
                    //p.hand.empower(c);
                }
                this.addToTop(new ApplyPowerAction(p, p, new RelayPower(p, cards)));
            }
        }
        this.tickDuration();
    }
}