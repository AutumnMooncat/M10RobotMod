package M10Robot.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnExtractCard {
    void onExtracted();
    void onExtractCard(AbstractCard c);
}
