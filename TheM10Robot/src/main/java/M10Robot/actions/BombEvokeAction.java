package M10Robot.actions;

import M10Robot.orbs.BombOrb;
import M10Robot.vfx.BurnToAshEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class BombEvokeAction extends AbstractGameAction {
    private final BombOrb orb;

    public BombEvokeAction(AbstractCreature source, BombOrb orb) {
        this.orb = orb;
        this.source = source;
    }

    @Override
    public void update() {
        orb.playAnimation(BombOrb.ATTACK_IMG, BombOrb.MED_ANIM);
        AbstractMonster t = null;
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (!mon.isDeadOrEscaped()) {
                if (t == null || mon.currentHealth > t.currentHealth) {
                    t = mon;
                }
            }
        }
        if (t != null) {
            int damage = orb.evokeAmount;
            if (t.hasPower(LockOnPower.POWER_ID)) {
                damage *= LockOnPower.MULTIPLIER;
            }
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(t.hb.cX, t.hb.cY));
            AbstractDungeon.effectsQueue.add(new BurnToAshEffect(t.hb.cX, t.hb.cY));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
            t.damage(new DamageInfo(source, damage, DamageInfo.DamageType.THORNS));
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.isDone = true;
    }
}
