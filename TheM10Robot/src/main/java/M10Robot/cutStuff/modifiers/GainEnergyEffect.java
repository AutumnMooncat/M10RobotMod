package M10Robot.cutStuff.modifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

@AbstractCardModifier.SaveIgnore
public class GainEnergyEffect extends AbstractSimpleStackingExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("GainEnergyEffect");

    public GainEnergyEffect(AbstractCard card) {
        super(ID, card, VariableType.MAGIC, false);
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
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new GainEnergyEffect(attachedCard.makeStatEquivalentCopy());
    }

}
