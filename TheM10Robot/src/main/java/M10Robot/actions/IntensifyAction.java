package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class IntensifyAction extends AbstractGameAction {
    public enum EffectType {
        DAMAGE,
        BLOCK,
        MAGIC
    }
    AbstractCard card;
    EffectType type;

    public IntensifyAction(AbstractCard card, int amount, EffectType type) {
        this.amount = amount;
        this.card = card;
        this.type = type;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> matches = new ArrayList<>();
        matches.add(card);
        matches.addAll(AbstractDungeon.player.hand.group.stream().filter(c -> c.getClass().equals(card.getClass())).collect(Collectors.toCollection(ArrayList::new)));
        matches.addAll(AbstractDungeon.player.drawPile.group.stream().filter(c -> c.getClass().equals(card.getClass())).collect(Collectors.toCollection(ArrayList::new)));
        matches.addAll(AbstractDungeon.player.discardPile.group.stream().filter(c -> c.getClass().equals(card.getClass())).collect(Collectors.toCollection(ArrayList::new)));

        for (AbstractCard c : matches) {
            applyVarChange(c);
        }

        this.isDone = true;
    }

    private void applyVarChange(AbstractCard c) {
        switch (type) {
            case DAMAGE:
                c.baseDamage += amount;
                break;
            case BLOCK:
                c.baseBlock += amount;
                break;
            case MAGIC:
                c.baseMagicNumber += amount;
                c.magicNumber += amount;
                break;
        }
        c.applyPowers();
//        if (c instanceof ModularDescription) {
//            c.initializeDescription();
//        }
    }
}
