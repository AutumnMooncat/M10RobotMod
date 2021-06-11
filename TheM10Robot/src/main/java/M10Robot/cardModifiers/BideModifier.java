package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.interfaces.ModularDescription;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BideModifier extends AbstractCardModifier {
    float duration = 0;
    float waitTime = 2f;
    int chargeLevel;
    int maxChargeLevel;
    int damageDelta, blockDelta, magicDelta, secondDelta, thirdDelta;

    public BideModifier(int maxChargeLevel, int damageDelta, int blockDelta, int magicDelta) {
        this(maxChargeLevel, damageDelta, blockDelta, magicDelta, 0, 0, 0);
    }

    public BideModifier(int maxChargeLevel, int damageDelta, int blockDelta, int magicDelta, int secondDelta, int thirdDelta) {
        this(maxChargeLevel, damageDelta, blockDelta, magicDelta, secondDelta, thirdDelta, 0);
    }

    private BideModifier(int maxChargeLevel, int damageDelta, int blockDelta, int magicDelta, int secondDelta, int thirdDelta, int chargeLevel) {
        this.maxChargeLevel = maxChargeLevel;
        this.damageDelta = damageDelta;
        this.blockDelta = blockDelta;
        this.magicDelta = magicDelta;
        this.secondDelta = secondDelta;
        this.thirdDelta = thirdDelta;
        this.chargeLevel = chargeLevel;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return M10RobotMod.getModID().toLowerCase()+":"+ BaseMod.getKeywordTitle("shadowsiren:multi-cost") + " " + maxChargeLevel + " NL " + rawDescription;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        resetEffect(card);
    }

    @Override
    public void onExhausted(AbstractCard card) {
        resetEffect(card);
    }

    @Override
    public void onUpdate(AbstractCard card) {/*
        if (AbstractDungeon.player != null) {
            if (getActivationCondition(card)) {
                duration += Gdx.graphics.getDeltaTime();
                if (duration > waitTime && maxChargeLevel > chargeLevel && EnergyPanel.totalCount > card.costForTurn) {
                    duration = 0;
                    chargeLevel++;
                    modifyVars(card, 1);
                    card.superFlash();
                    playSFX();
                }
            } else if (chargeLevel > 0 && !containedInQueue(card)) {
                modifyVars(card, -chargeLevel);
                //card.resetAttributes();
                card.applyPowers();
                chargeLevel = 0;
                duration = 0;
            }
        }*/
        if (AbstractDungeon.player != null) {
            clickUpdate(card);
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (chargeLevel > 0) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    resetEffect(card);
                    this.isDone = true;
                }
            });
        }
    }

    public void clickUpdate(AbstractCard card) {
        if (!AbstractDungeon.isScreenUp && HitboxRightClick.rightClicked.get(card.hb)) {
            onRightClick(card);
        }
    }

    public void onRightClick(AbstractCard card) {
        if (maxChargeLevel > chargeLevel /*&& EnergyPanel.totalCount > card.costForTurn*/) {
            incrementEffect(card);
        } else {
            resetEffect(card);
        }
    }

    private boolean containedInQueue(AbstractCard card) {
        for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card == card) {
                return true;
            }
        }
        return false;
    }

    private boolean getActivationCondition(AbstractCard card) {
        return AbstractDungeon.player.hoveredCard == card && AbstractDungeon.player.isHoveringDropZone && (!(card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY ) || hoveringMonster());
    }

    private boolean hoveringMonster() {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            m.hb.update();
            if (m.hb.hovered && !m.isDying && !m.isEscaping && m.currentHealth > 0) {
                return true;
            }
        }
        return false;
    }

    private void incrementEffect(AbstractCard card) {
        chargeLevel++;
        modifyVars(card, 1);
        card.superFlash();
        playSFX();
    }

    private void resetEffect(AbstractCard card) {
        modifyVars(card, -chargeLevel);
        //card.resetAttributes();
        card.applyPowers();
        chargeLevel = 0;
    }

    private void modifyVars(AbstractCard card, int delta) {
        modifyCost(card, delta);
        card.baseDamage += delta*damageDelta;
        card.baseBlock += delta*blockDelta;
        card.baseMagicNumber += delta*magicDelta;
        card.magicNumber += delta*magicDelta;
        if (card instanceof AbstractModdedCard) {
            modifyExtras((AbstractModdedCard) card, delta);
        }
        if (card instanceof ModularDescription) {
            ((ModularDescription) card).changeDescription();
        }
        card.applyPowers();
    }

    private void modifyExtras(AbstractModdedCard card, int delta) {
        card.baseSecondMagicNumber += delta*secondDelta;
        card.secondMagicNumber += delta*secondDelta;
        card.baseThirdMagicNumber += delta*thirdDelta;
        card.thirdMagicNumber += delta*thirdDelta;
    }

    private void modifyCost(AbstractCard card, int delta) {
        card.setCostForTurn(card.costForTurn + delta);
        card.isCostModifiedForTurn = card.costForTurn != card.cost;
    }

    private void playSFX() {
        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.1F);
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BideModifier(maxChargeLevel, damageDelta, blockDelta, magicDelta, secondDelta, thirdDelta, chargeLevel);
    }

    public static void resetMultiFormCard(AbstractCard card) {
        for (AbstractCardModifier m : CardModifierManager.modifiers(card)) {
            if (m instanceof BideModifier) {
                ((BideModifier) m).resetEffect(card);
            }
        }
    }
}
