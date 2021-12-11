package M10Robot.cutStuff.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BuffDamageAllCardsInHandAction extends AbstractGameAction {
    private final Color color;

    public BuffDamageAllCardsInHandAction(int amount, Color color) {
        this.actionType = ActionType.SPECIAL;
        this.amount = amount;
        this.color = color.cpy();
        this.startDuration = Settings.ACTION_DUR_XFAST;
        this.duration = this.startDuration;
    }

    public void update() {
        this.tickDuration();
        if (this.isDone) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.baseDamage >= 0) {
                    c.baseDamage += amount;
                    c.upgradedDamage = true;
                    c.flash(color);
                }
            }
        }
    }
}
