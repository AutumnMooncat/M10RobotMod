package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class PileDriver extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(PileDriver.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/

    public PileDriver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
        //this.addToBot(new VFXAction(new IntenseZoomEffect(Settings.WIDTH/2f, Settings.HEIGHT/2f, false)));
        //p.tint.changeColor(Color.ORANGE.cpy());
        float playerDrawXBackup = p.drawX;
        float playerDrawYBackup = p.drawY;
        float playerHitboxXBackup = p.hb.cX;
        float playerHitboxYBackup = p.hb.cY;
        float targetDrawXBackup = m.drawX;
        float targetDrawYBackup = m.drawY;
        float targetHitboxXBackup = m.hb.cX;
        float targetHitboxYBackup = m.hb.cY;
        AbstractMonster collisionMonster = AbstractDungeon.getRandomMonster(m);
        float collisionX, collisionY;
        if (collisionMonster != null) {
            collisionX = collisionMonster.hb.cX;
            collisionY = collisionMonster.hb.cY;
        } else {
            collisionX = targetHitboxXBackup;
            collisionY = targetHitboxYBackup;
        }
        final float speed = 150f * Settings.scale;
        float dst1;
        float closeEnough = 50f * Settings.scale;
        Vector2 tmp = new Vector2(targetHitboxXBackup - playerHitboxXBackup, targetHitboxYBackup - playerHitboxYBackup);
        dst1 = tmp.len();
        if (tmp.len() == 0) {
            tmp.set(1, 0);
        }
        tmp.nor();

        //Move to and grab the lad
        this.addToBot(new AbstractGameAction() {
            float currentSpeed = speed;
            float dx, dy;
            @Override
            public void update() {
                dx = tmp.x * currentSpeed;
                dy = tmp.y * currentSpeed;
                p.drawX += dx;
                p.drawY += dy;
                p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
                float dst2 = new Vector2(targetHitboxXBackup - p.hb.cX, targetHitboxYBackup - p.hb.cY).len();
                currentSpeed = speed*(dst2/dst1);
                if (dst2 < closeEnough) {
                    this.isDone = true;
                }
            }
        });

        //Lift both the lad and I off screen
        this.addToBot(new AbstractGameAction() {
            float dy;
            @Override
            public void update() {
                dy = speed;
                p.drawY += dy;
                p.hb.move(p.hb.cX,p.hb.cY+dy);
                m.drawY += dy;
                m.hb.move(m.hb.cX,m.hb.cY+dy);
                if (p.hb.cY > Settings.HEIGHT+p.hb.height && m.hb.cY > Settings.HEIGHT+m.hb.height) {
                    this.isDone = true;
                }
            }
        });

        //Teleport above the collision location and slam down
        this.addToBot(new AbstractGameAction() {
            boolean firstPass = true;
            float dy,dx;
            Vector2 vec;
            @Override
            public void update() {
                if (firstPass) {
                    //line us up properly
                    dx = collisionX-m.hb.cX;
                    p.drawX += dx;
                    p.hb.move(p.hb.cX+dx,p.hb.cY);
                    m.drawX += dx;
                    m.hb.move(m.hb.cX+dx,m.hb.cY);
                    //define the vector
                    vec = new Vector2(collisionX-m.hb.cX, collisionY-m.hb.cY).nor();
                    firstPass = false;
                }
                dx = limitMovement(speed*vec.x, collisionX-m.hb.cX);
                dy = limitMovement(speed*vec.y, collisionY-m.hb.cY);
                p.drawX += dx;
                p.drawY += dy;
                p.hb.move(p.hb.cX+dx,p.hb.cY+dy);
                m.drawX += dx;
                m.drawY += dy;
                m.hb.move(m.hb.cX+dx,m.hb.cY+dy);
                if ((m.hb.cY-collisionY) < closeEnough) {
                    this.isDone = true;
                }
            }
        });

        //Deal damage to appropriate targets and play sfx/vfx
        this.addToBot(new SFXAction("GHOST_ORB_IGNITE_2"));
        this.addToBot(new SFXAction("WATCHER_HEART_PUNCH"));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                this.isDone = true;
            }
        });
        this.addToBot(new DamageAction(m, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(m)], damageTypeForTurn),true));
        if (collisionMonster != null) {
            this.addToBot(new DamageAction(collisionMonster, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(collisionMonster)], damageTypeForTurn),true));
        }

        //Move everyone back to original locations
        this.addToBot(new AbstractGameAction() {
            boolean firstPass = true;
            float dxP,dyP,dxM,dyM;
            Vector2 playerVector;
            Vector2 monsterVector;
            @Override
            public void update() {
                if (firstPass) {
                    firstPass = false;
                    //playerVector = new Vector2(p.drawX-playerDrawXBackup, p.drawY-playerDrawYBackup).nor();
                    //monsterVector = new Vector2(m.drawX-targetDrawXBackup, m.drawY-targetDrawYBackup).nor();
                    playerVector = new Vector2(playerDrawXBackup-p.drawX, playerDrawYBackup-p.drawY).nor();
                    monsterVector = new Vector2(targetDrawXBackup-m.drawX, targetDrawYBackup-m.drawY).nor();
                }
                //dxP = limitMovement(speed*playerVector.x, p.drawX-playerDrawXBackup);
                //dyP = limitMovement(speed*playerVector.y, p.drawY-playerDrawYBackup);
                //dxM = limitMovement(speed*monsterVector.x, m.drawX-targetDrawXBackup);
                //dyM = limitMovement(speed*monsterVector.y, m.drawY-targetDrawYBackup);
                dxP = limitMovement(speed*playerVector.x, playerDrawXBackup-p.drawX);
                dyP = limitMovement(speed*playerVector.y, playerDrawYBackup-p.drawY);
                dxM = limitMovement(speed*monsterVector.x, targetDrawXBackup-m.drawX);
                dyM = limitMovement(speed*monsterVector.y, targetDrawYBackup-m.drawY);
                p.drawX += dxP;
                p.drawY += dyP;
                p.hb.move(p.hb.cX+dxP,p.hb.cY+dyP);
                m.drawX += dxM;
                m.drawY += dyM;
                m.hb.move(m.hb.cX+dxM,m.hb.cY+dyM);
                if (p.drawX == playerDrawXBackup && p.drawY == playerDrawYBackup && m.drawX == targetDrawXBackup && m.drawY == targetDrawYBackup) {
                    this.isDone = true;
                }
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
