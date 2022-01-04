package M10Robot.actions;

import M10Robot.orbs.BitOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static M10Robot.orbs.AbstractCustomOrb.MED_ANIM;

public class BitEvokeAction extends AbstractGameAction {
    private final BitOrb linkedOrb;
    private static final float DURATION = 0.1f;

    public BitEvokeAction(BitOrb linkedOrb, AbstractCreature source, int lockOn) {
        this.linkedOrb = linkedOrb;
        this.amount = lockOn;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            AbstractCreature t = AbstractDungeon.getRandomMonster();
            if (t != null) {
                int damage = linkedOrb.evokeAmount;
                if (t.hasPower(LockOnPower.POWER_ID)) {
                    damage *= LockOnPower.MULTIPLIER;
                }
                linkedOrb.playAnimation(BitOrb.ATTACK_IMG, MED_ANIM);
                AbstractDungeon.effectList.add(new ExplosionSmallEffect(t.hb.cX, t.hb.cY));
                AbstractDungeon.effectList.add(new OrbFlareEffect(linkedOrb, OrbFlareEffect.OrbFlareColor.DARK));
                DamageInfo di = new DamageInfo(source, damage, DamageInfo.DamageType.THORNS);
                t.damage(di);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
                this.addToTop(new ApplyPowerAction(t, source, new LockOnPower(t, amount), amount, true));
            } else {
                this.isDone = true;
                return;
            }
        }
        tickDuration();
    }
}
