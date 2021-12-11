package M10Robot.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SuperFastDamageAllEnemiesAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;

    public SuperFastDamageAllEnemiesAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, AttackEffect effect) {
        this.firstFrame = true;
        this.utilizeBaseDamage = false;
        this.source = source;
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        this.duration = 0.05f;
    }

    public SuperFastDamageAllEnemiesAction(AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AttackEffect effect) {
        this(player, null, type, effect);
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }

    @Override
    public void update() {
        if (this.firstFrame) {
            boolean playedMusic = false;
            final int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            }
            for (int i = 0; i < temp; ++i) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isDying && AbstractDungeon.getCurrRoom().monsters.monsters.get(i).currentHealth > 0 && !AbstractDungeon.getCurrRoom().monsters.monsters.get(i).isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect, true));
                    }
                    else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cX, AbstractDungeon.getCurrRoom().monsters.monsters.get(i).hb.cY, this.attackEffect));
                    }
                }
            }
            this.firstFrame = false;
        }
        this.tickDuration();
        if (this.isDone) {
            for (final AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }
            for (int temp2 = AbstractDungeon.getCurrRoom().monsters.monsters.size(), j = 0; j < temp2; ++j) {
                if (!AbstractDungeon.getCurrRoom().monsters.monsters.get(j).isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color.set(Color.CHARTREUSE);
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE) {
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.color.set(Color.RED);
                        AbstractDungeon.getCurrRoom().monsters.monsters.get(j).tint.changeColor(Color.WHITE.cpy());
                    }
                    AbstractDungeon.getCurrRoom().monsters.monsters.get(j).damage(new DamageInfo(this.source, this.damage[j], this.damageType));
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
