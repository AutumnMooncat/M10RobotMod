package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AbstractCardModifier.SaveIgnore
public class GainEnergyEffect extends AbstractExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("GainEnergyEffect");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = cardStrings.EXTENDED_DESCRIPTION;

    public GainEnergyEffect(AbstractCard card, int times) {
        super(card, VariableType.MAGIC, false, times);
        priority = 1;
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        addToBot(new GainEnergyAction(value));
    }

    @Override
    public boolean shouldRenderValue() {
        return value != 1;
    }

    @Override
    public String addExtraText(String rawDescription, AbstractCard card) {
        String s;
        if (value == 1) {
            s = TEXT[0];
        } else {
            s = TEXT[1] + key + TEXT[2];
        }
        return rawDescription + " NL " + s;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, ID)) {
            ((AbstractExtraEffectModifier) CardModifierManager.getModifiers(card, ID).get(0)).amount++;
            card.applyPowers();
            card.initializeDescription();
            return false;
        }
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        super.onApplyPowers(card);
        baseValue *= amount;
        value *= amount;
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        super.onCalculateCardDamage(card, mo);
        baseValue *= amount;
        value *= amount;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new GainEnergyEffect(attachedCard, amount);
    }

}
