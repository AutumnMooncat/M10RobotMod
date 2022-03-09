package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.powers.SpikesPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpikesMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("SpikesMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private int amount = 0;

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.baseDamage > 1) {
            amount += card.baseDamage;
            modifyBaseStat(card, BuffType.DAMAGE, BuffScale.MINOR_DEBUFF);
            amount -= card.baseDamage;
        }
        if (card.baseBlock > 1) {
            amount += card.baseBlock;
            modifyBaseStat(card, BuffType.BLOCK, BuffScale.MINOR_DEBUFF);
            amount -= card.baseBlock;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && (card.baseDamage > 1 || card.baseBlock > 1);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], amount);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpikesPower(AbstractDungeon.player, amount)));
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SpikesMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
