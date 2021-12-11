package M10Robot.cutCards.modifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempBlockModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempBlockModifier");

    public TempBlockModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempBlockModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseBlock += amount;
        card.applyPowers();
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.baseBlock -= amount;
        card.applyPowers();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractBoosterModifier)CardModifierManager.getModifiers(card, ID).get(0)).amount += amount;
            card.baseBlock += amount;
            card.applyPowers();
            return false;
        }
        return true;
    }

    @Override
    public boolean unstack(AbstractCard card, int stacksToUnstack) {
        if (CardModifierManager.hasModifier(card, ID)) {
            AbstractBoosterModifier mod = (AbstractBoosterModifier) CardModifierManager.getModifiers(card, ID).get(0);
            mod.amount -= stacksToUnstack;
            card.baseBlock -= stacksToUnstack;
            card.applyPowers();
            if (mod.amount <= 0) {
                CardModifierManager.removeSpecificModifier(card, mod, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSuffix() {
        return TEXT[1]+amount;
    }
}