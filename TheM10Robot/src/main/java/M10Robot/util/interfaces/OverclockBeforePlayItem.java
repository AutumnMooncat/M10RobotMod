package M10Robot.util.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OverclockBeforePlayItem {
    int overclockAmount(AbstractCard card);
    void onOverclock(AbstractCard card);
}
