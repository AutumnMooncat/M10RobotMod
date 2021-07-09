package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.interfaces.RequiresSingleTargetAimingMode;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.PoisonPower;

@AbstractCardModifier.SaveIgnore
public class ApplyPoisonEffect extends AbstractSimpleStackingExtraEffectModifier implements RequiresSingleTargetAimingMode {
    public static final String ID = M10RobotMod.makeID("ApplyPoisonEffect");

    public ApplyPoisonEffect(AbstractCard card) {
        super(ID, card, VariableType.MAGIC, false);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, value)));
    }

    @Override
    public boolean shouldRenderValue() {
        return true;
    }

    @Override
    public String addExtraText(String rawDescription, AbstractCard card) {
        String s;
        s = TEXT[0] + key + TEXT[1];
        return rawDescription + " NL " + s;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ApplyPoisonEffect(attachedCard.makeStatEquivalentCopy());
    }

}

