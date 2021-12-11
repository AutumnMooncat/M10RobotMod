package M10Robot.cutStuff.modifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempMagicNumberModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempMagicNumberModifier");

    public TempMagicNumberModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempMagicNumberModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseMagicNumber += amount;
        card.magicNumber = card.baseMagicNumber;
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.baseMagicNumber -= amount;
        card.magicNumber = card.baseMagicNumber;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractBoosterModifier)CardModifierManager.getModifiers(card, ID).get(0)).amount += amount;
            card.baseMagicNumber += amount;
            card.magicNumber = card.baseMagicNumber;
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
            card.magicNumber = card.baseMagicNumber;
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