package M10Robot.cardModifiers;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractSimpleStackingExtraEffectModifier extends AbstractExtraEffectModifier {

    public AbstractSimpleStackingExtraEffectModifier(String ID, AbstractCard card, VariableType type, boolean isMutable) {
        super(ID, card, type, isMutable, 1);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, identifier(card))) {
            AbstractExtraEffectModifier mod = ((AbstractExtraEffectModifier)CardModifierManager.getModifiers(card, identifier(card)).get(0));
            mod.attachedCard.magicNumber += attachedCard.magicNumber;
            mod.attachedCard.baseMagicNumber += attachedCard.magicNumber;
            card.applyPowers();
            return false;
        }
        return true;
    }

    @Override
    public boolean unstack(AbstractCard card, int stacksToUnstack) {
        if (CardModifierManager.hasModifier(card, identifier(card))) {
            AbstractExtraEffectModifier mod = ((AbstractExtraEffectModifier)CardModifierManager.getModifiers(card, identifier(card)).get(0));
            mod.attachedCard.magicNumber -= stacksToUnstack;
            mod.attachedCard.baseMagicNumber -= stacksToUnstack;
            card.applyPowers();
            if (mod.attachedCard.magicNumber <= 0) {
                CardModifierManager.removeSpecificModifier(card, mod, true);
                AbstractExtraEffectModifier.clearAndRecreateRegister(card);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        super.onApplyPowers(card);
        card.initializeDescription();
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        super.onCalculateCardDamage(card, mo);
        card.initializeDescription();
    }

    public int getAmount() {
        return attachedCard.magicNumber;
    }
}
