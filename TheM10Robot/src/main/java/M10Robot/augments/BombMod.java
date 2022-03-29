package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.cards.Byte;
import M10Robot.orbs.BombOrb;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class BombMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("BombMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int ORBS = 1;

    public void onInitialApplication(AbstractCard card) {
        if (card.baseDamage > 1) {
            modifyBaseStat(card, BuffType.DAMAGE, BuffScale.MODERATE_DEBUFF);
        }

        if (card.baseBlock > 1) {
            modifyBaseStat(card, BuffType.BLOCK, BuffScale.MODERATE_DEBUFF);
        }
        card.showEvokeValue = true;
    }

    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && allowOrbMods() && (card.baseDamage > 1 || card.baseBlock > 1);
    }

    public String modifyName(String cardName, AbstractCard card) {
        String[] nameParts = removeUpgradeText(cardName);
        return TEXT[0] + nameParts[0] + TEXT[1] + nameParts[1];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], ORBS);
    }

    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new ChannelAction(new BombOrb()));
    }

    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    public AbstractCardModifier makeCopy() {
        return new BombMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
