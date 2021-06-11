package M10Robot.cardModifiers;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BoosterValueModifier extends AbstractBoosterModifier {
    int damageDelta, blockDelta, magicDelta, secondDelta, thirdDelta;
    boolean effectApplied;

    public BoosterValueModifier(int damageDelta, int blockDelta, int magicDelta, int secondDelta, int thirdDelta) {
        this.damageDelta = damageDelta;
        this.blockDelta = blockDelta;
        this.magicDelta = magicDelta;
        this.secondDelta = secondDelta;
        this.thirdDelta = thirdDelta;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        playSFX();
    }

    private void playSFX() {
        CardCrawlGame.sound.play("CARD_UPGRADE", 0.1F);
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return super.modifyDamage(damage, type, card, target) + damageDelta;
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        return super.modifyBlock(block, card) + blockDelta;
    }

    //Deal with magic numbers and other dynvars here, but make sure to use the boolean check
    @Override
    public void onApplyPowers(AbstractCard card) {
        if (!effectApplied) {
            applyOtherEffects(card);
        }
        super.onApplyPowers(card);
    }

    @Override
    public void onDrawn(AbstractCard card) {
        if (!effectApplied) {
            applyOtherEffects(card);
        }
        super.onDrawn(card);
    }

    private void applyOtherEffects(AbstractCard card) {
        card.magicNumber += magicDelta;
        card.isMagicNumberModified = card.baseMagicNumber != card.magicNumber;
        if (card instanceof AbstractModdedCard) {
            ((AbstractModdedCard) card).secondMagicNumber += secondDelta;
            ((AbstractModdedCard) card).isSecondMagicNumberModified = ((AbstractModdedCard) card).baseSecondMagicNumber != ((AbstractModdedCard) card).secondMagicNumber;
            ((AbstractModdedCard) card).thirdMagicNumber += thirdDelta;
            ((AbstractModdedCard) card).isThirdMagicNumberModified = ((AbstractModdedCard) card).baseThirdMagicNumber != ((AbstractModdedCard) card).thirdMagicNumber;
        }
        effectApplied = true;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if(group != AbstractDungeon.player.hand) {
            effectApplied = false;
        }
        super.atEndOfTurn(card, group);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BoosterValueModifier(damageDelta, blockDelta, magicDelta, secondDelta, thirdDelta);
    }
}
