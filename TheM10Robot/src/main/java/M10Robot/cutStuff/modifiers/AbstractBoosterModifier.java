package M10Robot.cutStuff.modifiers;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class AbstractBoosterModifier extends AbstractCardModifier {
    protected String internalID;
    protected final CardStrings cardStrings;
    protected final String[] TEXT;
    protected int amount;

    public AbstractBoosterModifier(String ID) {
        super();
        this.internalID = ID;
        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        this.TEXT = cardStrings.EXTENDED_DESCRIPTION;
        this.priority = AbstractBoosterPriorityLookup.getPriorityIndex(this);
    }

    public abstract boolean unstack(AbstractCard card, int stacksToUnstack);

    public String getPrefix() {
        return "";
    }

    public String getSuffix() {
        return "";
    }

    public int getAmount() {
        return amount;
    }
}
