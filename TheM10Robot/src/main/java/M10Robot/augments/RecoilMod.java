package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.powers.RecoilPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RecoilMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("RecoilMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int EFFECT = 1;

    @Override
    public void onInitialApplication(AbstractCard card) {
        modifyBaseStat(card, BuffType.DAMAGE, BuffScale.HUGE_BUFF);
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && card.type == AbstractCard.CardType.ATTACK && card.baseDamage > 0;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], EFFECT);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RecoilPower(AbstractDungeon.player, EFFECT)));
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RecoilMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
