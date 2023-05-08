package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.cards.Byte;
import M10Robot.orbs.BombOrb;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BombMod extends AbstractAugment {
    public static final String ID = M10RobotMod.makeID(BombMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] EXTRA_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    private static final int ORBS = 1;

    public void onInitialApplication(AbstractCard card) {
        card.showEvokeValue = true;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage * MODERATE_DEBUFF;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return block * MODERATE_DEBUFF;
    }

    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && allowOrbMods() && (card.baseDamage > 1 || card.baseBlock > 1);
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
    public String getAugmentDescription() {
        return EXTRA_TEXT[0];
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
