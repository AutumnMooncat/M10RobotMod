package M10Robot.augments;

import CardAugments.cardmods.AbstractAugment;
import CardAugments.cardmods.DynvarCarrier;
import M10Robot.M10RobotMod;
import M10Robot.powers.SpikesPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpikesMod extends AbstractAugment implements DynvarCarrier {
    public static final String ID = M10RobotMod.makeID(SpikesMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] EXTRA_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private static final String KEY = "!" + ID + "!";

    private int damageComponent = 0;
    private int blockComponent = 0;
    private boolean setBaseVar;

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        blockComponent = (int) Math.ceil(block * (1 - MINOR_DEBUFF));
        return block * MINOR_DEBUFF;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        damageComponent = (int) Math.ceil(damage * (1 - MINOR_DEBUFF));
        return damage * MINOR_DEBUFF;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.rawDescription.contains(TEXT[4])) {
            setBaseVar = true;
            return magic + damageComponent + blockComponent;
        }
        return magic;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && (card.baseDamage > 1 || card.baseBlock > 1);
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

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (setBaseVar) {
            return rawDescription;
        }
        return insertAfterText(rawDescription , String.format(TEXT[2], KEY));
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!setBaseVar) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpikesPower(AbstractDungeon.player, damageComponent + blockComponent)));
        }
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

    @Override
    public String key() {
        return ID;
    }

    @Override
    public int val(AbstractCard abstractCard) {
        return damageComponent + blockComponent;
    }

    @Override
    public int baseVal(AbstractCard abstractCard) {
        return damageComponent + blockComponent;
    }

    @Override
    public boolean modified(AbstractCard abstractCard) {
        return false;
    }

    @Override
    public boolean upgraded(AbstractCard abstractCard) {
        return abstractCard.upgradedDamage || abstractCard.upgradedBlock;
    }
}
