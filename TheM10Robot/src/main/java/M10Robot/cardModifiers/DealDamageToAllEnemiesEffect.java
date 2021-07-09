package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

@AbstractCardModifier.SaveIgnore
public class DealDamageToAllEnemiesEffect extends AbstractExtraEffectModifier {
    private static final String ID = M10RobotMod.makeID("DealDamageToAllEnemiesEffect");

    public DealDamageToAllEnemiesEffect(AbstractCard card, int times) {
        super(ID, card, VariableType.DAMAGE, false, times);
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m) {
        for (int i = 0 ; i < amount ; i++) {
            addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(value), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE, true));
        }
    }

    @Override
    public String addExtraText(String rawDescription, AbstractCard card) {
        String s = TEXT[0] + key + TEXT[1];
        s = applyTimes(s);
        return rawDescription + " NL " + s;
    }

    @Override
    public boolean shouldRenderValue() {
        return true;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        //If we already have this modifier
        if (CardModifierManager.hasModifier(card, ID)) {
            //Grab the base value offset
            int offset = 0;
            if (CardModifierManager.hasModifier(card, TempBlockModifier.ID)) {
                //Get a list of all the base value effects and loop add to the offset
                ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, TempDamageModifier.ID);
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
                //If the base damage of the card from the mod is equal to the base damage of our new mod (plus the offset that will get added)
                if (c.baseDamage == attachedCard.baseDamage+offset) {
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
            if (CardModifierManager.hasModifier(card, TempDamageModifier.ID)) {
                //Get a list of all the base value effects and loop add to the offset
                ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, TempDamageModifier.ID);
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
                if (c.baseDamage == (attachedCard.baseDamage+offset)) {
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
    public AbstractCardModifier makeCopy() {
        return new DealDamageToAllEnemiesEffect(attachedCard.makeStatEquivalentCopy(), amount);
    }
}
