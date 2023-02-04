package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

public class AimedMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID(AimedMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final float MULTI = 4/3F;
    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && card.baseDamage > 0 && card.type == AbstractCard.CardType.ATTACK && allowOrbMods();
    }

    @Override
    public float modifyDamageFinal(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (target == null) {
            return damage;
        }
        return target.hasPower(LockOnPower.POWER_ID) ? damage * MULTI : damage;
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSufix() {
        return TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[2];
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AimedMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public CardBorderGlowManager.GlowInfo getGlowInfo() {
        return new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return hasThisMod(abstractCard) && AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.hasPower(LockOnPower.POWER_ID));
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.GOLD.cpy();
            }

            @Override
            public String glowID() {
                return ID+"Glow";
            }
        };
    }
}
