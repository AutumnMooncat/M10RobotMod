package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class SplitBeamAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST;
    private final AbstractCard card;

    public SplitBeamAction(AbstractCard card) {
        this.card = card;
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
    }

    public void update() {
        if (duration == DURATION) {
            this.target = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (this.target != null) {
                this.card.calculateCardDamage((AbstractMonster)this.target);
                CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT", 0.5F);
                AbstractDungeon.effectList.add(new SmallLaserEffect(target.hb.cX, target.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
                DamageInfo di = new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn);
                target.damage(di);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
        tickDuration();
    }
}
