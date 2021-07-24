package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.patches.RestorePositionPatches;
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

import static M10Robot.M10RobotMod.makeCardPath;

public class PileDriver extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(PileDriver.class.getSimpleName());
    public static final String IMG = makeCardPath("PileDriver.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public PileDriver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.setBackUp(p, p.drawX, p.drawY, p.hb.cX, p.hb.cY);
                this.isDone = true;
            }
        });

        this.addToBot(new AbstractGameAction() {
            final float playerDrawXBackup = p.drawX;
            final float playerDrawYBackup = p.drawY;
            final float playerHitboxXBackup = p.hb.cX;
            final float playerHitboxYBackup = p.hb.cY;
            final float targetDrawXBackup = m.drawX;
            final float targetDrawYBackup = m.drawY;
            final float targetHitboxXBackup = m.hb.cX;
            final float targetHitboxYBackup = m.hb.cY;
            final AbstractMonster collisionMonster = AbstractDungeon.getRandomMonster(m);
            final float speed = 150f * Settings.scale;
            final float closeEnough = 50f * Settings.scale;
            float currentSpeed = speed;
            float collisionX, collisionY;
            float dst1;
            float dx, dy, dxM, dyM;
            int actionPhase;
            final Vector2 grabVector = new Vector2(targetHitboxXBackup - playerHitboxXBackup, targetHitboxYBackup - playerHitboxYBackup);
            Vector2 downVector;
            Vector2 playerVector;
            Vector2 monsterVector;
            boolean firstPass = true;
            boolean targetDied = false;
            float waitTimer = 0f;
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
                    p.drawX += dx;
                    p.drawY += dy;
                    p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
                    float dst2 = new Vector2(targetHitboxXBackup - p.hb.cX, targetHitboxYBackup - p.hb.cY).len();
                    currentSpeed = speed*(dst2/dst1);
                    if (dst2 < closeEnough) {
                        //Close enough to grab, next phase
                        actionPhase++;
                    }
                } else if (actionPhase == 1) {
                    //Lift the target up off the screen
                    dy = speed;
                    p.drawY += dy;
                    p.hb.move(p.hb.cX,p.hb.cY+dy);
                    m.drawY += dy;
                    m.hb.move(m.hb.cX,m.hb.cY+dy);
                    if (p.hb.cY > Settings.HEIGHT+p.hb.height && m.hb.cY > Settings.HEIGHT+m.hb.height) {
                        //Up high enough, next phase
                        actionPhase++;
                        //Teleport above the target monster so we can move straight down next phase
                        dx = collisionX-m.hb.cX;
                        p.drawX += dx;
                        p.hb.move(p.hb.cX+dx,p.hb.cY);
                        m.drawX += dx;
                        m.hb.move(m.hb.cX+dx,m.hb.cY);
                        //define the vector
                        downVector = new Vector2(collisionX-m.hb.cX, collisionY-m.hb.cY).nor();
                        //add a pause
                        waitTimer = 0.3f;
                    }
                } else if (actionPhase == 2) {
                    //Move down onto the target monster
                    dx = limitMovement(speed* downVector.x, collisionX-m.hb.cX);
                    dy = limitMovement(speed* downVector.y, collisionY-m.hb.cY);
                    p.drawX += dx;
                    p.drawY += dy;
                    p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
                    m.drawX += dx;
                    m.drawY += dy;
                    m.hb.move(m.hb.cX+dx,m.hb.cY+dy);
                    if ((m.hb.cY-collisionY) < closeEnough) {
                        //Close enough
                        actionPhase++;
                    }
                } else if (actionPhase == 3) {
                    //Deal damage to appropriate targets and play sfx/vfx
                    CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.15f);
                    CardCrawlGame.sound.play("WATCHER_HEART_PUNCH", 0.15f);
                    AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
                    m.damage(new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(m)], damageTypeForTurn));
                    if (collisionMonster != null && collisionMonster != m) {
                        collisionMonster.damage(new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(collisionMonster)], damageTypeForTurn));
                    }
                    //Check if the target died so we dont need to move it
                    if (m.isDeadOrEscaped()) {
                        targetDied = true;
                    }
                    //Move the the next phase and set up our vectors also slight pause
                    actionPhase++;
                    playerVector = new Vector2(playerDrawXBackup-p.drawX, playerDrawYBackup-p.drawY).nor();
                    monsterVector = new Vector2(targetDrawXBackup-m.drawX, targetDrawYBackup-m.drawY).nor();
                    waitTimer = 0.1f;
                } else if (actionPhase == 4) {
                    //Move the player (and monster if its alive) back to the original positions
                    dx = limitMovement(speed*playerVector.x, playerDrawXBackup-p.drawX);
                    dy = limitMovement(speed*playerVector.y, playerDrawYBackup-p.drawY);
                    p.drawX += dx;
                    p.drawY += dy;
                    p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
                    if (!targetDied) {
                        dxM = limitMovement(speed*monsterVector.x, targetDrawXBackup-m.drawX);
                        dyM = limitMovement(speed*monsterVector.y, targetDrawYBackup-m.drawY);
                        m.drawX += dxM;
                        m.drawY += dyM;
                        m.hb.move(m.hb.cX+dxM,m.hb.cY+dyM);
                    }
                    if (p.drawX == playerDrawXBackup && p.drawY == playerDrawYBackup && (targetDied || (m.drawX == targetDrawXBackup && m.drawY == targetDrawYBackup))) {
                        this.isDone = true;
                    }
                }
                if (this.isDone) {
                    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                        AbstractDungeon.actionManager.clearPostCombatActions();
                    }
                }
            }
        });

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.removeBackUp(p);
                this.isDone = true;
            }
        });
    }

    public float limitMovement(float desiredSpeed, float maxSpeed) {
        return (Math.abs(desiredSpeed) > Math.abs(maxSpeed)) ? maxSpeed : desiredSpeed;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
