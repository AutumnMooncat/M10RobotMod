package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

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
            ((AbstractBoosterModifier)CardModifierManager.getModifiers(card, ID).get(0)).amount -= stacksToUnstack;
            card.baseMagicNumber -= stacksToUnstack;
            card.magicNumber = card.baseMagicNumber;
            card.applyPowers();
            card.initializeDescription();;
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