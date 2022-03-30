package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.cards.SpikeBall;
import M10Robot.cards.SteelWall;
import M10Robot.orbs.SearchlightOrb;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SearchMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("SearchMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int ORBS = 1;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        modifyBaseStat(card, BuffType.BLOCK, BuffScale.MODERATE_DEBUFF);
        if (card instanceof SteelWall || card instanceof SpikeBall) {
            card.baseMagicNumber += ORBS;
            card.magicNumber += ORBS;
            setBaseVar = true;
        }
        card.showEvokeValue = true;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && allowOrbMods() && card.baseBlock > 1;
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
            this.addToBot(new ChannelAction(new SearchlightOrb()));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SearchMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
