package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractReloadableCard;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ReloadingModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("ReloadingModifier");
    public static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = STRINGS.EXTENDED_DESCRIPTION;

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card instanceof AbstractReloadableCard) {
            if (((AbstractReloadableCard) card).needsReload) {
                rawDescription = TEXT[0] + rawDescription;
            } else {
                rawDescription = TEXT[1] + rawDescription;
            }
        }
        return rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ReloadingModifier();
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
