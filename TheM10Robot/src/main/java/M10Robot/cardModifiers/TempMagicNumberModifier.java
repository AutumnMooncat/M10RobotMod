package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class TempMagicNumberModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempMagicNumberModifier");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = cardStrings.EXTENDED_DESCRIPTION;

    public TempMagicNumberModifier(int increase) {
        this.increase = increase;
        this.priority = -3;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempMagicNumberModifier(increase);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseMagicNumber += increase;
        card.magicNumber = card.baseMagicNumber;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.baseMagicNumber -= increase;
        card.magicNumber = card.baseMagicNumber;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((TempMagicNumberModifier)CardModifierManager.getModifiers(card, ID).get(0)).increase += increase;
            card.baseMagicNumber += increase;
            card.magicNumber = card.baseMagicNumber;
            return false;
        }
        return true;
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSuffix() {
        return TEXT[1]+increase;
    }
}