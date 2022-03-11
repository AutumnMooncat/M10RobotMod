package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class SplitBeamAction extends AbstractGameAction {
    private final AbstractCard card;

    public SplitBeamAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster();
        if (this.target != null) {
            this.card.calculateCardDamage((AbstractMonster)this.target);
            this.addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), AttackEffect.NONE));
            this.addToTop(new VFXAction(new SmallLaserEffect(target.hb.cX, target.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
            this.addToTop(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        }

        this.isDone = true;
    }
}
