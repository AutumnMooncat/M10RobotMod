package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.cards.AssemblyLine;
import M10Robot.cards.Byte;
import M10Robot.cards.CompileData;
import M10Robot.orbs.BitOrb;
import M10Robot.orbs.PresentOrb;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class PresentMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("PresentMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int ORBS = 1;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card instanceof AssemblyLine) {
            card.baseMagicNumber += ORBS;
            card.magicNumber += ORBS;
            setBaseVar = true;
        }
        if (card instanceof CompileData) {
            ((CompileData) card).baseSecondMagicNumber += ORBS;
            ((CompileData) card).secondMagicNumber += ORBS;
            setBaseVar = true;
        }
        card.cost = card.cost + 1;
        card.costForTurn = card.cost;
        card.showEvokeValue = true;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost >= 0 && doesntUpgradeCost(card) && allowOrbMods() && card.rarity != AbstractCard.CardRarity.BASIC && card.rarity != AbstractCard.CardRarity.COMMON;
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
        return rawDescription + String.format(TEXT[2], ORBS);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!setBaseVar) {
            this.addToBot(new ChannelAction(new PresentOrb()));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.RARE;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PresentMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
