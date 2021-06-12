package M10Robot.actions;

import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.patches.BoosterFieldPatch;
import M10Robot.cards.interfaces.SwappableCard;
import M10Robot.patches.EmpowerRedirectPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;

public class EquipBoosterAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final AbstractBoosterCard boosterCard;
    private final AbstractCard targetCard;
    private final AbstractPlayer p;

    public EquipBoosterAction(AbstractBoosterCard boosterCard, AbstractCard targetCard) {
        this.boosterCard = boosterCard;
        this.targetCard = targetCard;
        p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && p.hand.contains(boosterCard)) {
            for (AbstractBoosterModifier mod : boosterCard.getBoosterModifiers()) {
                CardModifierManager.addModifier(targetCard, mod);
                BoosterFieldPatch.equipBooster(targetCard);
                if (targetCard instanceof SwappableCard && targetCard.cardsToPreview != null) {
                    CardModifierManager.addModifier(targetCard.cardsToPreview, mod.makeCopy());
                    BoosterFieldPatch.equipBooster(targetCard.cardsToPreview);
                }
            }
            targetCard.applyPowers();
            targetCard.superFlash();
            if (targetCard instanceof SwappableCard && targetCard.cardsToPreview != null) {
                targetCard.applyPowers();
                targetCard.superFlash();;
            }
            AbstractDungeon.player.hand.removeCard(boosterCard);
            AbstractDungeon.player.onCardDrawOrDiscard();
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.effectList.add(new EmpowerEffect(targetCard.hb.cX, targetCard.hb.cY));
                    this.isDone = true;
                }
            });
        }
        this.tickDuration();
    }
}