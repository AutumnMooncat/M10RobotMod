package M10Robot.cutStuff.modifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

@AbstractCardModifier.SaveIgnore
public class LoseOrbSlotEffect extends AbstractSimpleStackingExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("LoseOrbSlotEffect");

    public LoseOrbSlotEffect(AbstractCard card) {
        super(ID, card, VariableType.MAGIC, false);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        addToBot(new DecreaseMaxOrbAction(value));
    }

    @Override
    public boolean shouldRenderValue() {
        return true;
    }

    @Override
    public String addExtraText(String rawDescription, AbstractCard card) {
        String s;
        if (value == 1) {
            s = TEXT[0] + key + TEXT[1];
        } else {
            s = TEXT[0] + key + TEXT[2];
        }

        return rawDescription + " NL " + s;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LoseOrbSlotEffect(attachedCard.makeStatEquivalentCopy());
    }

}

