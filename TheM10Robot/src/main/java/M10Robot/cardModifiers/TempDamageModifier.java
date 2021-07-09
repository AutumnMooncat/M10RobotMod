package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class TempDamageModifier extends AbstractValueBuffModifier {
    public static final String ID = M10RobotMod.makeID("TempDamageModifier");

    public TempDamageModifier(int increase) {
        super(ID, increase);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TempDamageModifier(amount);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.baseDamage += amount;
        card.applyPowers();
    }

    @Override
    public void onRemove(AbstractCard card) {
        card.baseDamage -= amount;
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
            card.baseDamage += amount;
            card.applyPowers();
            return false;
        }
        return true;
    }

    @Override
    public boolean unstack(AbstractCard card, int stacksToUnstack) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractBoosterModifier)CardModifierManager.getModifiers(card, ID).get(0)).amount -= stacksToUnstack;
            card.baseDamage -= stacksToUnstack;
            card.applyPowers();
        }
        return amount <= 0;
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