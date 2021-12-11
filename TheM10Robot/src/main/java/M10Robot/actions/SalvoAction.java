package M10Robot.actions;

import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.patches.LockOrbAnimationPatches;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import java.util.HashMap;

public class SalvoAction extends AbstractGameAction {
    boolean firstPass = true;
    final HashMap<AbstractOrb, Vector2> vecMap = new HashMap<>();
    final HashMap<AbstractOrb, Vector2> offsetMap = new HashMap<>();
    final HashMap<AbstractOrb, Float> backupX = new HashMap<>();
    final HashMap<AbstractOrb, Float> backupY = new HashMap<>();
    final HashMap<AbstractOrb, Boolean> isDoneMap = new HashMap<>();
    final float mainSpeed = 75 * Settings.scale;
    final float offsetSpeed = 25 * Settings.scale;
    float dx,dy;
    AbstractMonster m;
    AbstractPlayer p;
    DamageInfo info;
    boolean removeOrbs;

    public SalvoAction(AbstractPlayer player, AbstractMonster monster, DamageInfo info, boolean removeOrbs) {
        this.p = player;
        this.m = monster;
        this.info = info;
        this.removeOrbs = removeOrbs;
    }

    @Override
    public void update() {
        if (firstPass) {
            for (AbstractOrb o : p.orbs) {
                if (o instanceof EmptyOrbSlot) {
                    isDoneMap.put(o, true);
                } else {
                    vecMap.put(o, new Vector2(m.hb.cX-o.cX, m.hb.cY-o.cY).nor());
                    offsetMap.put(o, new Vector2(MathUtils.random(2f)-1f, MathUtils.random(2f)-1f).nor());
                    backupX.put(o, o.cX);
                    backupY.put(o, o.cY);
                    isDoneMap.put(o, false);
                    if (o instanceof AbstractCustomOrb && ((AbstractCustomOrb) o).attackImage != null) {
                        ((AbstractCustomOrb) o).playAnimation(((AbstractCustomOrb) o).attackImage, AbstractCustomOrb.MED_ANIM);
                    }
                    LockOrbAnimationPatches.StopAnimatingField.stopAnimating.set(o, true);
                }
            }
            firstPass = false;
        }
        this.isDone = true;
        for (AbstractOrb o : p.orbs) {
            if (!isDoneMap.get(o)) {
                this.isDone = false;
                vecMap.get(o).set(m.hb.cX-o.cX, m.hb.cY-o.cY).nor();
                dx = limitMovement(mainSpeed*vecMap.get(o).x + offsetSpeed*offsetMap.get(o).x, m.hb.cX-o.cX);
                dy = limitMovement(mainSpeed*vecMap.get(o).y + offsetSpeed*offsetMap.get(o).y, m.hb.cY-o.cY);
                o.cX += dx;
                o.cY += dy;
                o.hb.move(o.hb.cX+dx,o.hb.cY+dy);
                if (o.cX == m.hb.cX && o.cY == m.hb.cY) {
                    CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.1f);
                    AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                    isDoneMap.put(o, true);
                    m.damage(info);
                }
            }
        }
        if (this.isDone) {
            for (AbstractOrb o : p.orbs) {
                if (!(o instanceof EmptyOrbSlot)) {
                    o.cX = backupX.get(o);
                    o.cY = backupY.get(o);
                    LockOrbAnimationPatches.StopAnimatingField.stopAnimating.set(o, false);
                    //p.removeNextOrb();
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
            if (removeOrbs) {
                while (!(p.orbs.get(0) instanceof EmptyOrbSlot)) {
                    p.removeNextOrb();
                }
            }
        }
    }

    public float limitMovement(float desiredSpeed, float maxSpeed) {
        return (Math.abs(desiredSpeed) > Math.abs(maxSpeed)) ? maxSpeed : desiredSpeed;
    }
}
