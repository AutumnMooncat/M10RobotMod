package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class TempDamageModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempDamageModifier");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = cardStrings.EXTENDED_DESCRIPTION;

    public TempDamageModifier(int increase) {
        this.increase = increase;
        this.priority = -4;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempDamageModifier(increase);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage += increase;
        card.applyPowers();
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.baseDamage -= increase;
        card.applyPowers();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((TempDamageModifier)CardModifierManager.getModifiers(card, ID).get(0)).increase += increase;
            card.baseDamage += increase;
            card.applyPowers();
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