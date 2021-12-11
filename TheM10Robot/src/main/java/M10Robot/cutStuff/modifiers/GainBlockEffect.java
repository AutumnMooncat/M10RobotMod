package M10Robot.cutStuff.modifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;

@AbstractCardModifier.SaveIgnore
public class GainBlockEffect extends AbstractExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("GainBlockEffect");

    public GainBlockEffect(AbstractCard card, int times) {
        super(ID, card, VariableType.BLOCK, false, times);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        for (int i = 0; i < amount; ++i) {
            addToBot(new GainBlockAction(p, p, value, true));
        }
    }

    @Override
    public String addExtraText(String rawDescription, AbstractCard card) {
        String s = TEXT[0] + key + TEXT[1];
        s = applyTimes(s);
        return rawDescription + " NL " + s;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        //If we already have a GainBlockEffect mod
        if (CardModifierManager.hasModifier(card, ID)) {
            //Grab the base value offset
            int offset = 0;
            if (CardModifierManager.hasModifier(card, TempBlockModifier.ID)) {
                //Get a list of all the base value effects and loop add to the offset
                ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, TempBlockModifier.ID);
                for (AbstractCardModifier mod : list) {
                    offset += ((AbstractValueBuffModifier)mod).amount;
                }
            }
            //Get a list of all the gain effects
            ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, ID);
            //Loop through them
            for (AbstractCardModifier mod : list) {
                //Grab the attached card from the mod
                AbstractCard c = ((AbstractExtraEffectModifier)mod).attachedCard;
                //If the base block of the card from the mod is equal to the base block of our new mod (plus the offset that will get added)
                if (c.baseBlock == (attachedCard.baseBlock+offset)) {
                    //Increment the amount instead
                    ((AbstractExtraEffectModifier)mod).amount++;
                    card.applyPowers();
                    card.initializeDescription();
                    return false;
                }
            }
        }
        //Else apply it as a new mod
        return true;
    }

    @Override
    public boolean unstack(AbstractCard card, int stacksToUnstack) {
        //If we have a GainBlockEffect mod
        if (CardModifierManager.hasModifier(card, ID)) {
            //Grab the base value offset
            int offset = 0;
            if (CardModifierManager.hasModifier(card, TempBlockModifier.ID)) {
                //Get a list of all the base value effects and loop add to the offset
                ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, TempBlockModifier.ID);
                for (AbstractCardModifier mod : list) {
                    offset += ((AbstractValueBuffModifier)mod).amount;
                }
            }
            //Get a list of all the gain effects
            ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, ID);
            //Loop through them
            for (AbstractCardModifier mod : list) {
                //Grab the attached card from the mod
                AbstractCard c = ((AbstractExtraEffectModifier)mod).attachedCard;
                //If the base block of the card from the mod is equal to the base block of our mod (plus the offset that will get added)
                if (c.baseBlock == (attachedCard.baseBlock+offset)) {
                    //Reduce the amount
                    ((AbstractExtraEffectModifier)mod).amount -= stacksToUnstack;
                    card.applyPowers();
                    card.initializeDescription();
                    //remove it if it hit 0
                    if (((AbstractExtraEffectModifier)mod).amount <= 0) {
                        CardModifierManager.removeSpecificModifier(card, mod, true);
                        AbstractExtraEffectModifier.clearAndRecreateRegister(card);
                        return true;
                    }
                }
            }
        }
        //Else we did nothing
        return false;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldRenderValue() {
        return true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new GainBlockEffect(attachedCard.makeStatEquivalentCopy(), amount);
    }

    @Override
    public String getPrefix() {
        if (amount > 1) {
            return TEXT[2]+amount;
        }
        return TEXT[2];
    }

    @Override
    public String getSuffix() {
        return TEXT[3];
    }
}