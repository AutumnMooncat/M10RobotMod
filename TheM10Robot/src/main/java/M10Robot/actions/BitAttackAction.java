package M10Robot.actions;

import M10Robot.orbs.BitOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static M10Robot.orbs.AbstractCustomOrb.MED_ANIM;

public class BitAttackAction extends AbstractGameAction {
    private final BitOrb linkedOrb;
    private static final float DURATION = 0.1f;

    public BitAttackAction(BitOrb linkedOrb, AbstractCreature source) {
        this(linkedOrb, source, null);
    }

    public BitAttackAction(BitOrb linkedOrb, AbstractCreature source, AbstractCreature target) {
        this.linkedOrb = linkedOrb;
        this.source = source;
        this.target = target;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            AbstractCreature t = target;
            if (t == null) {
                for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                    if (!mon.isDeadOrEscaped()) {
                        if (t == null || mon.currentHealth < t.currentHealth) {
                            t = mon;
                        }
                    }
                }
            }
            if (t != null) {
                int damage = linkedOrb.passiveAmount;
                if (t.hasPower(LockOnPower.POWER_ID)) {
                    damage *= LockOnPower.MULTIPLIER;
                }
                linkedOrb.playAnimation(BitOrb.ATTACK_IMG, MED_ANIM);
                CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT", 0.5F);
                AbstractDungeon.effectList.add(new SmallLaserEffect(t.hb.cX, t.hb.cY, linkedOrb.getXPosition(), linkedOrb.getYPosition()));
                AbstractDungeon.effectList.add(new OrbFlareEffect(linkedOrb, OrbFlareEffect.OrbFlareColor.FROST));
                DamageInfo di = new DamageInfo(source, damage, DamageInfo.DamageType.THORNS);
                t.damage(di);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            } else {
                this.isDone = true;
                return;
            }
        }
        tickDuration();
    }
}
