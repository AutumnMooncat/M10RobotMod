package M10Robot.actions;

import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.powers.ModulesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class EquipModuleAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private final AbstractModuleCard card;
    private final AbstractPlayer p;

    public EquipModuleAction(AbstractModuleCard card) {
        this.card = card;
        p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.POWER;
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && p.hand.contains(card)) {
            if (p.hasPower(ModulesPower.POWER_ID)) {
                ModulesPower pow = (ModulesPower) p.getPower(ModulesPower.POWER_ID);
                if (pow.canEquip()) {
                    pow.equipModule(card);
                    p.hand.empower(card);
                } else {
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, ModulesPower.getCantEquipMessage(), true));
                }
            } else {
                ModulesPower pow = new ModulesPower(p, 1);
                pow.equipModule(card);
                p.hand.empower(card);
                this.addToTop(new ApplyPowerAction(p, p, pow));
            }
        }
        this.tickDuration();
    }
}