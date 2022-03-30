package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.actions.UpgradeOrbsAction;
import M10Robot.cards.BoosterShot;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BoostMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID("BoostMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int UPGRADES = 2;
    private boolean setBaseVar;

    @Override
    public void onInitialApplication(AbstractCard card) {
        modifyBaseStat(card, BuffType.DAMAGE, BuffScale.MAJOR_DEBUFF);
        if (card instanceof BoosterShot) {
            card.baseMagicNumber += UPGRADES;
            card.magicNumber += UPGRADES;
            setBaseVar = true;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return M10RobotMod.enableChimeraCrossover && card.cost != -2 && allowOrbMods() && card.baseDamage > 1 && card.type == AbstractCard.CardType.ATTACK;
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
        return rawDescription + String.format(TEXT[2], UPGRADES);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!setBaseVar) {
            if (!AbstractDungeon.player.orbs.isEmpty()) {
                this.addToBot(new UpgradeOrbsAction(AbstractDungeon.player.orbs.get(0), UPGRADES));
            }
        }
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.UNCOMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BoostMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
