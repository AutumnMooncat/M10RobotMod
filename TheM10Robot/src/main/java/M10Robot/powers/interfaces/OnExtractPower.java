package M10Robot.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnExtractPower {
    int modifyExtractAmount(int amount);
    void onExtractCard(AbstractCard c);
}
