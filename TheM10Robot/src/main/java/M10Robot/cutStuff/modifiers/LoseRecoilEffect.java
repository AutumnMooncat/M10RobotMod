package M10Robot.cutStuff.modifiers;

import M10Robot.M10RobotMod;
import M10Robot.powers.RecoilPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

@AbstractCardModifier.SaveIgnore
public class LoseRecoilEffect extends AbstractSimpleStackingExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("LoseRecoilEffect");

    public LoseRecoilEffect(AbstractCard card) {
        super(ID, card, VariableType.MAGIC, false);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        addToBot(new ApplyPowerAction(p, p, new RecoilPower(p, -value)));
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
        return new LoseRecoilEffect(attachedCard.makeStatEquivalentCopy());
    }

}
