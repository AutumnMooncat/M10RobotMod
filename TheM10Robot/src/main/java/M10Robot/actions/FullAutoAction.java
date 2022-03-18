package M10Robot.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class FullAutoAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;

    public FullAutoAction(AbstractCreature source, int[] damages, DamageInfo.DamageType type, AttackEffect effect) {
        this.firstFrame = true;
        this.utilizeBaseDamage = false;
        this.source = source;
        this.damage = damages;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        this.duration = 0.05f;
    }

    public FullAutoAction(AbstractPlayer player, int baseDamage, DamageInfo.DamageType type, AttackEffect effect) {
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
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(j);
                if (!m.isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        m.tint.color.set(Color.CHARTREUSE);
                        m.tint.changeColor(Color.WHITE.cpy());
                    }
                    else if (this.attackEffect == AttackEffect.FIRE) {
                        m.tint.color.set(Color.RED);
                        m.tint.changeColor(Color.WHITE.cpy());
                    }
                    m.damage(new DamageInfo(this.source, this.damage[j], this.damageType));
                    if (m.lastDamageTaken > 0) {
                        ApplyPowerAction a = new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, -m.lastDamageTaken), -m.lastDamageTaken, true);
                        addToBot(a);
                        addToBot(new DoIfPowerAppliedAction(a, new ApplyPowerAction(m, AbstractDungeon.player, new GainStrengthPower(m, m.lastDamageTaken), m.lastDamageTaken, true)));
                    }
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
