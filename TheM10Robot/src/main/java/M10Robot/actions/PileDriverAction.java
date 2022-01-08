package M10Robot.actions;

import com.badlogic.gdx.Gdx;
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

public class PileDriverAction extends AbstractGameAction {
    static final float SPEED = 150f * Settings.scale;
    static final float CLOSE_ENOUGH = 50f * Settings.scale;
    static final float TAKING_TOO_LONG = 1f;
    final float playerDrawXBackup;
    final float playerDrawYBackup;
    final float playerHitboxXBackup;
    final float playerHitboxYBackup;
    final float targetDrawXBackup;
    final float targetDrawYBackup;
    final float targetHitboxXBackup;
    final float targetHitboxYBackup;
    final AbstractMonster targetMonster;
    final AbstractMonster collisionMonster;
    float currentSpeed = SPEED;
    float collisionX, collisionY;
    float dst1;
    float dx, dy, dxM, dyM;
    final Vector2 grabVector;
    Vector2 downVector;
    Vector2 playerVector;
    Vector2 monsterVector;
    boolean firstPass = true;
    float skipTimer = 0F;
    float waitTimer = 0f;
    int[] damages;
    int actionPhase;
    DamageInfo.DamageType damageType;
    
    public PileDriverAction(AbstractPlayer source, AbstractMonster target, int[] damages, DamageInfo.DamageType damageType) {
        this.source = source;    
        this.targetMonster = target;
        this.damages = damages;
        this.damageType = damageType;
        playerDrawXBackup = source.drawX;
        playerDrawYBackup = source.drawY;
        playerHitboxXBackup = source.hb.cX;
        playerHitboxYBackup = source.hb.cY;
        targetDrawXBackup = targetMonster.drawX;
        targetDrawYBackup = targetMonster.drawY;
        targetHitboxXBackup = targetMonster.hb.cX;
        targetHitboxYBackup = targetMonster.hb.cY;
        collisionMonster = AbstractDungeon.getRandomMonster(targetMonster);
        grabVector = new Vector2(targetHitboxXBackup - playerHitboxXBackup, targetHitboxYBackup - playerHitboxYBackup);
    }
    
    @Override
    public void update() {
        if (firstPass) {
            //Initialize variables on the first pass
            firstPass = false;
            if (collisionMonster != null) {
                collisionX = collisionMonster.hb.cX;
                collisionY = collisionMonster.hb.cY;
            } else {
                collisionX = targetHitboxXBackup;
                collisionY = targetHitboxYBackup;
            }
            dst1 = grabVector.len();
            if (grabVector.len() == 0) {
                grabVector.set(1, 0);
            }
            grabVector.nor();
            actionPhase = 0;
        }
        if (waitTimer > 0) {
            waitTimer -= Gdx.graphics.getDeltaTime();
        } else if (actionPhase == 0) {
            //Move towards to target to grab them
            dx = grabVector.x * currentSpeed;
            dy = grabVector.y * currentSpeed;
            source.drawX += dx;
            source.drawY += dy;
            source.hb.move(source.hb.cX+dx,source.hb.cY+dy);
            float dst2 = new Vector2(targetHitboxXBackup - source.hb.cX, targetHitboxYBackup - source.hb.cY).len();
            currentSpeed = SPEED *(dst2/dst1);
            skipTimer += Gdx.graphics.getDeltaTime();
            if (dst2 <= CLOSE_ENOUGH || skipTimer >= TAKING_TOO_LONG) {
                //Close enough to grab, next phase
                actionPhase++;
                skipTimer = 0f;
            }
        } else if (actionPhase == 1) {
            //Lift the target up off the screen
            dy = SPEED;
            source.drawY += dy;
            source.hb.move(source.hb.cX,source.hb.cY+dy);
            targetMonster.drawY += dy;
            targetMonster.hb.move(targetMonster.hb.cX,targetMonster.hb.cY+dy);
            skipTimer += Gdx.graphics.getDeltaTime();
            if ((source.hb.cY > Settings.HEIGHT+source.hb.height && targetMonster.hb.cY > Settings.HEIGHT+targetMonster.hb.height) || skipTimer >= TAKING_TOO_LONG) {
                //Up high enough, next phase
                actionPhase++;
                //Teleport above the target monster so we can move straight down next phase
                dx = collisionX-targetMonster.hb.cX;
                source.drawX += dx;
                source.hb.move(source.hb.cX+dx,source.hb.cY);
                targetMonster.drawX += dx;
                targetMonster.hb.move(targetMonster.hb.cX+dx,targetMonster.hb.cY);
                //define the vector
                downVector = new Vector2(collisionX-targetMonster.hb.cX, collisionY-targetMonster.hb.cY).nor();
                //add a pause
                waitTimer = 0.3f;
                skipTimer = 0f;
            }
        } else if (actionPhase == 2) {
            //Move down onto the target monster
            dx = limitMovement(SPEED * downVector.x, collisionX-targetMonster.hb.cX);
            dy = limitMovement(SPEED * downVector.y, collisionY-targetMonster.hb.cY);
            source.drawX += dx;
            source.drawY += dy;
            source.hb.move(source.hb.cX+dx,source.hb.cY+dy);
            targetMonster.drawX += dx;
            targetMonster.drawY += dy;
            targetMonster.hb.move(targetMonster.hb.cX+dx,targetMonster.hb.cY+dy);
            skipTimer += Gdx.graphics.getDeltaTime();
            if ((targetMonster.hb.cY-collisionY) <= CLOSE_ENOUGH || skipTimer >= TAKING_TOO_LONG) {
                //Close enough
                actionPhase++;
                skipTimer = 0f;
            }
        } else if (actionPhase == 3) {
            //Deal damage to appropriate targets and play sfx/vfx
            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.15f);
            CardCrawlGame.sound.play("WATCHER_HEART_PUNCH", 0.15f);
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(targetMonster.hb.cX, targetMonster.hb.cY));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
            targetMonster.damage(new DamageInfo(source, damages[AbstractDungeon.getMonsters().monsters.indexOf(targetMonster)], damageType));
            if (collisionMonster != null && collisionMonster != targetMonster) {
                collisionMonster.damage(new DamageInfo(source, damages[AbstractDungeon.getMonsters().monsters.indexOf(collisionMonster)], damageType));
            }
            //Move the the next phase and set up our vectors also slight pause
            actionPhase++;
            playerVector = new Vector2(playerDrawXBackup-source.drawX, playerDrawYBackup-source.drawY).nor();
            monsterVector = new Vector2(targetDrawXBackup-targetMonster.drawX, targetDrawYBackup-targetMonster.drawY).nor();
            waitTimer = 0.1f;
        } else if (actionPhase == 4) {
            skipTimer += Gdx.graphics.getDeltaTime();
            //Move the player (and monster if its alive) back to the original positions
            dx = limitMovement(SPEED *playerVector.x, playerDrawXBackup-source.drawX);
            dy = limitMovement(SPEED *playerVector.y, playerDrawYBackup-source.drawY);
            source.drawX += dx;
            source.drawY += dy;
            source.hb.move(source.hb.cX+dx,source.hb.cY+dy);
            dxM = limitMovement(SPEED *monsterVector.x, targetDrawXBackup-targetMonster.drawX);
            dyM = limitMovement(SPEED *monsterVector.y, targetDrawYBackup-targetMonster.drawY);
            targetMonster.drawX += dxM;
            targetMonster.drawY += dyM;
            targetMonster.hb.move(targetMonster.hb.cX+dxM,targetMonster.hb.cY+dyM);
            if (skipTimer >= TAKING_TOO_LONG) {
                fixTheCoordinates();
                this.isDone = true;
            }
            if (source.drawX == playerDrawXBackup && source.drawY == playerDrawYBackup && targetMonster.drawX == targetDrawXBackup && targetMonster.drawY == targetDrawYBackup) {
                this.isDone = true;
            }
        }
        if (this.isDone) {
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }

    public float limitMovement(float desiredSpeed, float maxSpeed) {
        return (Math.abs(desiredSpeed) > Math.abs(maxSpeed)) ? maxSpeed : desiredSpeed;
    }

    public void fixTheCoordinates() {
        source.drawX = playerDrawXBackup;
        source.drawY = playerDrawYBackup;
        source.hb.move(playerHitboxXBackup, playerHitboxYBackup);
        targetMonster.drawX = targetDrawXBackup;
        targetMonster.drawY = targetDrawYBackup;
        targetMonster.hb.move(targetHitboxXBackup, targetHitboxYBackup);
    }
}
