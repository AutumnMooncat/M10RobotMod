package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.cards.Byte;
import M10Robot.cards.Nibble;
import M10Robot.orbs.BitOrb;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class BitMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("BitMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int ORBS = 1;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        modifyBaseStat(card, BuffType.DAMAGE, BuffScale.MODERATE_DEBUFF);
        if (card instanceof Nibble) {
            card.magicNumber += ORBS;
            setBaseVar = true;
        }
        if (card instanceof Byte) {
            ((Byte)card).baseSecondMagicNumber += ORBS;
            setBaseVar = true;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && allowOrbMods() && card.baseDamage > 1;
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
            this.addToBot(new ChannelAction(new BitOrb()));
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BitMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
