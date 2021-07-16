package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.patches.RepeatFields;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempRepeatModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempRepeatModifier");

    public TempRepeatModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + TEXT[0] + amount + TEXT[1];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempRepeatModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        RepeatFields.baseRepeat.set(card, RepeatFields.baseRepeat.get(card) + amount);
        RepeatFields.repeat.set(card, RepeatFields.baseRepeat.get(card));
        card.applyPowers();
    }

    @Override
    public void onRemove(AbstractCard card) {
        RepeatFields.baseRepeat.set(card, RepeatFields.baseRepeat.get(card) - amount);
        RepeatFields.repeat.set(card, RepeatFields.baseRepeat.get(card));
        card.applyPowers();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractBoosterModifier) CardModifierManager.getModifiers(card, ID).get(0)).amount += amount;
            RepeatFields.baseRepeat.set(card, RepeatFields.baseRepeat.get(card) + amount);
            RepeatFields.repeat.set(card, RepeatFields.baseRepeat.get(card));
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
            RepeatFields.baseRepeat.set(card, RepeatFields.baseRepeat.get(card) - stacksToUnstack);
            RepeatFields.repeat.set(card, RepeatFields.baseRepeat.get(card));
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
        return TEXT[2];
    }

    @Override
    public String getSuffix() {
        return TEXT[3] + amount;
    }

}
