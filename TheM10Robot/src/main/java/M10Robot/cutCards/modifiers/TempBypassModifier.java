package M10Robot.cutCards.modifiers;

import M10Robot.M10RobotMod;
import M10Robot.patches.CostBypassField;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempBypassModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempBypassModifier");

    public TempBypassModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + TEXT[0] + TEXT[1];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempBypassModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        CostBypassField.bypassCost.set(card, true);
        card.applyPowers();
        card.initializeDescription();
    }

    @Override
    public void onRemove(AbstractCard card) {
        CostBypassField.bypassCost.set(card, false);
        card.applyPowers();
        card.initializeDescription();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractBoosterModifier) CardModifierManager.getModifiers(card, ID).get(0)).amount += amount;
            card.applyPowers();
            card.initializeDescription();
            return false;
        }
        return true;
    }

    @Override
    public boolean unstack(AbstractCard card, int stacksToUnstack) {
        if (CardModifierManager.hasModifier(card, ID)) {
            AbstractBoosterModifier mod = (AbstractBoosterModifier) CardModifierManager.getModifiers(card, ID).get(0);
            mod.amount -= stacksToUnstack;
            card.applyPowers();
            card.initializeDescription();
            if (mod.amount <= 0) {
                CostBypassField.bypassCost.set(card, false);
                CardModifierManager.removeSpecificModifier(card, mod, true);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPrefix() {
        return TEXT[2];
    }

    @Override
    public String getSuffix() {
        return TEXT[3] + amount;
    }

}
