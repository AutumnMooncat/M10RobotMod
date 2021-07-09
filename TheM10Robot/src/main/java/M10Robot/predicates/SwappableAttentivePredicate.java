package M10Robot.predicates;

import M10Robot.cards.abstractCards.AbstractSwappableCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.function.Predicate;

public class SwappableAttentivePredicate {
    Predicate<AbstractCard> p;

    public SwappableAttentivePredicate(Predicate<AbstractCard> p) {
        this.p = p;
    }

    public boolean testPredicatesOnSwappable(AbstractCard c) {
        boolean b = p.test(c);
        if (c instanceof AbstractSwappableCard && c.cardsToPreview != null) {
            b |= p.test(c.cardsToPreview);
        }
        return b;
    }
}
