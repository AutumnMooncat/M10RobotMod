package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.actions.ExtractAction;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ExtractMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("ExtractMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int EXTRACT = 1;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        modifyBaseStat(card, BuffType.BLOCK, BuffScale.MINOR_DEBUFF);
        if (card.rawDescription.contains(TEXT[4])) {
            card.baseMagicNumber += EXTRACT;
            card.magicNumber += EXTRACT;
            setBaseVar = true;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && card.baseBlock > 1;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (setBaseVar) {
            return rawDescription;
        }
        return rawDescription + String.format(TEXT[2], EXTRACT);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!setBaseVar) {
            this.addToBot(new ExtractAction(EXTRACT));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExtractMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
