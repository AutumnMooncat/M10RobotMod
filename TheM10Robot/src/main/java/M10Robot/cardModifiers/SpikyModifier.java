package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.powers.SpikesPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

public class SpikyModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("SpikyModifier");
    /*public static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = STRINGS.EXTENDED_DESCRIPTION;*/

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (AbstractDungeon.player.hasPower(SpikesPower.POWER_ID)) {
            damage += AbstractDungeon.player.getPower(SpikesPower.POWER_ID).amount;
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
