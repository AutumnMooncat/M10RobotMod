package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

public class HeuristicMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID(HeuristicMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final int LOCK_ON = 1;

    @Override
    public void onInitialApplication(AbstractCard card) {
        DamageModifierManager.addModifier(card, new HeuristicDamage(LOCK_ON));
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage * MINOR_DEBUFF;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && card.baseDamage > 1 && card.type == AbstractCard.CardType.ATTACK && allowOrbMods();
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSuffix() {
        return TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + String.format(TEXT[2], LOCK_ON);
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HeuristicMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    public static class HeuristicDamage extends AbstractDamageModifier {
        int amount;

        public HeuristicDamage(int amount) {
            this.amount = amount;
        }

        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            if (lastDamageTaken > 0 && info.owner != target) {
                this.addToBot(new ApplyPowerAction(target, info.owner, new LockOnPower(target, amount)));
            }
        }

        @Override
        public boolean isInherent() {
             return true;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new HeuristicDamage(amount);
        }
    }
}
