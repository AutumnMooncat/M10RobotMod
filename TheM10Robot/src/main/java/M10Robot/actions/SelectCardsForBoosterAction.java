package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectCardsForBoosterAction extends AbstractGameAction {
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;
    private String text;
    private boolean anyNumber;
    private boolean canPickZero;
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;

    public boolean screenWasOpened = false;

    public SelectCardsForBoosterAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.text = textForSelect;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.predicate = cardFilter;
        this.callback = callback;
        this.hand = AbstractDungeon.player.hand.group;
        this.tempHand = new ArrayList<>();
        this.tempHand.addAll(this.hand);
    }

    public SelectCardsForBoosterAction(int amount, String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, cardFilter, callback);
    }

    public SelectCardsForBoosterAction(int amount, String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, false, false, (c) -> {
            return true;
        }, callback);
    }

    public SelectCardsForBoosterAction(String textForSelect, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, cardFilter, callback);
    }

    public SelectCardsForBoosterAction(String textForSelect, Consumer<List<AbstractCard>> callback) {
        this(1, textForSelect, false, false, (c) -> {
            return true;
        }, callback);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.hand.size() != 0 && !this.hand.stream().noneMatch(this.predicate) && this.callback != null) {
                if (this.hand.stream().filter(this.predicate).count() <= (long)this.amount && !this.anyNumber && !this.canPickZero) {
                    this.callback.accept(this.hand.stream().filter(this.predicate).collect(Collectors.toList()));
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                    this.isDone = true;
                } else {
                    this.tempHand.removeIf(this.predicate);
                    if (this.tempHand.size() > 0) {
                        this.hand.removeIf((c) -> {
                            return this.tempHand.contains(c);
                        });
                    }
                    screenWasOpened = true;
                    AbstractDungeon.handCardSelectScreen.open(this.text, this.amount, this.anyNumber, this.canPickZero, false, true);
                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            this.callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            this.hand.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (this.tempHand.size() > 0) {
                this.hand.addAll(this.tempHand);
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}