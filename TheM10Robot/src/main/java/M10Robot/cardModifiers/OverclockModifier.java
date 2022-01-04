package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OverclockModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("Overclock");
    public  static final int PERCENT_PER_AMOUNT = 25;

    int rawPercent;

    public OverclockModifier() {
        this(1);
    }
    public OverclockModifier(int amount) {
        this.rawPercent = PERCENT_PER_AMOUNT * amount;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card instanceof AbstractSwappableCard && card.cardsToPreview != null) {
            CardModifierManager.removeWhenPlayedModifiers(card.cardsToPreview);
        }
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.applyPowers();
        card.initializeDescription();
    }

    public int getRawPercent() {
        return rawPercent;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, identifier(card))) {
            OverclockModifier mod = ((OverclockModifier)CardModifierManager.getModifiers(card, identifier(card)).get(0));
            mod.rawPercent += this.rawPercent;
            card.applyPowers();
            card.initializeDescription();
            return false;
        }
        return true;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        card.magicNumber = (int) (card.baseMagicNumber * (100F + rawPercent) / 100F);
        card.isMagicNumberModified = card.magicNumber != card.baseMagicNumber;
        if (card instanceof AbstractModdedCard) {
            ((AbstractModdedCard) card).secondMagicNumber = (int) (((AbstractModdedCard) card).baseSecondMagicNumber * (100F + rawPercent) / 100F);
            ((AbstractModdedCard) card).isSecondMagicNumberModified = ((AbstractModdedCard) card).secondMagicNumber != ((AbstractModdedCard) card).baseSecondMagicNumber;
            //((AbstractModdedCard) card).thirdMagicNumber = (int) (((AbstractModdedCard) card).baseThirdMagicNumber * (100F + rawPercent) / 100F);
            //((AbstractModdedCard) card).isThirdMagicNumberModified = ((AbstractModdedCard) card).thirdMagicNumber != ((AbstractModdedCard) card).baseThirdMagicNumber;
        }
    }

    @Override
    public float modifyDamageFinal(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return (damage * (100F + rawPercent)) / 100F;
    }

    @Override
    public float modifyBlockFinal(float block, AbstractCard card) {
        return (block * (100F + rawPercent)) / 100F;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new OverclockModifier(rawPercent/PERCENT_PER_AMOUNT);
    }
}
