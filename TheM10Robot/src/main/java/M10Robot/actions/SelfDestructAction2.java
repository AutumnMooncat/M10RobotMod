package M10Robot.actions;

import M10Robot.vfx.BurnToAshEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.ArrayList;

public class SelfDestructAction2 extends AbstractGameAction {
    final float xBb;
    final float yBd;
    final float xBh;
    final float yBh;
    final float tx;
    final float ty;
    final float speed = 50f * Settings.scale;
    final float damageFalloffIncrements = 80f * Settings.scale;
    float dst1;
    Vector2 tmp;
    float currentSpeed = speed;
    float dx, dy;
    boolean firstPass = true;
    int actionPhase;
    float waitTimer = 0f;
    AbstractMonster m;
    AbstractPlayer p;
    DamageInfo info;
    int hpLoss;

    public SelfDestructAction2(AbstractPlayer player, AbstractMonster monster, DamageInfo info, int hpLoss) {
        this.p = player;
        this.m = monster;
        this.info = info;
        this.hpLoss = hpLoss;
        xBb = p.drawX;
        yBd = p.drawY;
        xBh = p.hb.cX;
        yBh = p.hb.cY;
        tx = m.hb.cX;
        ty = m.hb.cY;
        tmp = new Vector2(tx - p.hb.cX, ty - p.hb.cY);
    }

    @Override
    public void update() {
        if(firstPass) {
            firstPass = false;
            dst1 = tmp.len();
            if (tmp.len() == 0) {
                tmp.set(1, 0);
            }
            tmp.nor();
            p.tint.changeColor(Color.ORANGE.cpy());
            actionPhase = 0;
            CardCrawlGame.sound.play("ORB_PLASMA_CHANNEL", 0.15f);
            AbstractDungeon.effectsQueue.add(new IntenseZoomEffect(Settings.WIDTH/2f, Settings.HEIGHT/2f, false));
        }
        if (waitTimer > 0) {
            waitTimer -= Gdx.graphics.getDeltaTime();
        } else if (actionPhase == 0) {
            //Move towards the target
            dx = tmp.x * currentSpeed;
            dy = tmp.y * currentSpeed;
            p.drawX += dx;
            p.drawY += dy;
            p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
            float dst2 = new Vector2(tx - p.hb.cX, ty - p.hb.cY).len();
            currentSpeed = speed*(dst2/dst1);
            if (currentSpeed <= 3f || dst2 < damageFalloffIncrements) {
                //Close enough, or we slowed down enough
                actionPhase++;
            }
        } else if (actionPhase == 1) {
            //VFX and SFX time
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(p.hb.cX, p.hb.cY));
            AbstractDungeon.effectsQueue.add(new BurnToAshEffect(p.hb.cX, p.hb.cY));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.15f);
            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.15f);
            CardCrawlGame.sound.play("WATCHER_HEART_PUNCH", 0.15f);
            if (hpLoss > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, AttackEffect.FIRE));
                p.damage(new DamageInfo(p, hpLoss, DamageInfo.DamageType.HP_LOSS));
            }
            AbstractDungeon.effectsQueue.add(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.RED.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC));
            m.damage(info);
            p.drawX = -2*xBb;
            p.drawY = yBd;
            p.tint.changeColor(Color.WHITE.cpy());
            p.hb.move(xBh, yBh);
            //advance the action
            actionPhase++;
        } else if (actionPhase == 2) {
            //Move back to normal positions
            p.drawX += Math.min(speed, xBb-p.drawX);
            p.drawY = yBd;
            if (p.drawX == xBb) {
                this.isDone = true;
            }
        }
        if (this.isDone) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
