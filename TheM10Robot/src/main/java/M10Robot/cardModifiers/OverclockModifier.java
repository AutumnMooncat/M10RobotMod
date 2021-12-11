package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModdedCard;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OverclockModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("Overclock");
    private static final int EARLY_PERCENT = 25;
    private static final int MID_PERCENT = 20;
    private static final int LATE_PERCENT = 15;

    int rawPercent;
    int count;

    public OverclockModifier() {
        this(1);
    }
    public OverclockModifier(int count) {
        this.count = count;
        rawPercent = getRawPercent();
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.applyPowers();
    }

    public int getOverclocks() {
        return count;
    }

    public int getRawPercent() {
        int percent = 0;
        for (int i = 0 ; i < count ; i++) {
            if (i < 4) {
                percent += EARLY_PERCENT;
            } else if (i < 9) {
                percent += MID_PERCENT;
            } else {
                percent += LATE_PERCENT;
            }
        }
        return percent;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, identifier(card))) {
            OverclockModifier mod = ((OverclockModifier)CardModifierManager.getModifiers(card, identifier(card)).get(0));
            mod.count += this.count;
            mod.rawPercent = mod.getRawPercent();
            card.applyPowers();
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
            ((AbstractModdedCard) card).thirdMagicNumber = (int) (((AbstractModdedCard) card).baseThirdMagicNumber * (100F + rawPercent) / 100F);
            ((AbstractModdedCard) card).isThirdMagicNumberModified = ((AbstractModdedCard) card).thirdMagicNumber != ((AbstractModdedCard) card).baseThirdMagicNumber;
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
        return new OverclockModifier(count);
    }
}
