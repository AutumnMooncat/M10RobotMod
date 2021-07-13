package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.RefundFields;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TempRefundModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempRefundModifier");

    public TempRefundModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + TEXT[0] + amount + TEXT[1];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempRefundModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        RefundFields.baseRefund.set(card, RefundFields.baseRefund.get(card) + amount);
        RefundFields.refund.set(card, RefundFields.baseRefund.get(card));
        card.applyPowers();
    }

    @Override
    public void onRemove(AbstractCard card) {
        RefundFields.baseRefund.set(card, RefundFields.baseRefund.get(card) - amount);
        RefundFields.refund.set(card, RefundFields.baseRefund.get(card));
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
            RefundFields.baseRefund.set(card, RefundFields.baseRefund.get(card) + amount);
            RefundFields.refund.set(card, RefundFields.baseRefund.get(card));
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
            RefundFields.baseRefund.set(card, RefundFields.baseRefund.get(card) - stacksToUnstack);
            RefundFields.refund.set(card, RefundFields.baseRefund.get(card));
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
