package M10Robot.relics.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnExtractRelic {
    int modifyExtractAmount(int amount);
    void onExtractCard(AbstractCard c);
}
