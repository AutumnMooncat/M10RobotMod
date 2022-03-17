package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

public class AimedModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("AimedModifier");
    /*public static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = STRINGS.EXTENDED_DESCRIPTION;*/

    @Override
    public float modifyDamageFinal(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (target != null && target.hasPower(LockOnPower.POWER_ID)) {
            damage *= LockOnPower.MULTIPLIER;
        }
        return damage;
    }

    /*@Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        rawDescription = TEXT[0] + rawDescription;
        return rawDescription;
    }*/

    @Override
    public AbstractCardModifier makeCopy() {
        return new AimedModifier();
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
