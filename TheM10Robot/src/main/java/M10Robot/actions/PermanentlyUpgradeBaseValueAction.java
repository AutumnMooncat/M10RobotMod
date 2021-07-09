package M10Robot.actions;

import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.powers.ModulesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.Iterator;
@Deprecated
public class PermanentlyUpgradeBaseValueAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final AbstractCard card;
    private final AbstractPlayer p;
    private final int damageAmount, blockAmount, magicAmount;

    public PermanentlyUpgradeBaseValueAction(AbstractCard card, int damageAmount, int blockAmount, int magicAmount) {
        this.card = card;
        this.damageAmount = damageAmount;
        this.blockAmount = blockAmount;
        this.magicAmount = magicAmount;
        p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION) {
            for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(card.uuid)) {
                    c.baseDamage += damageAmount;
                    c.baseBlock += blockAmount;
                    c.magicNumber += magicAmount;
                    c.baseMagicNumber += magicAmount;
                    c.applyPowers();
                }
            }
            for (AbstractCard c : GetAllInBattleInstances.get(card.uuid)) {
                if (c.uuid.equals(card.uuid)) {
                    c.baseDamage += damageAmount;
                    c.baseBlock += blockAmount;
                    c.magicNumber += magicAmount;
                    c.baseMagicNumber += magicAmount;
                    c.applyPowers();
                }
            }
        }
        this.tickDuration();
    }
}