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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;
import java.util.HashMap;

import static M10Robot.M10RobotMod.makeCardPath;

public class Rushdown extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Rushdown.class.getSimpleName());
    public static final String IMG = makeCardPath("RushDown.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;

    // /STAT DECLARATION/

    public Rushdown() {
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
            final float xB = p.drawX;
            final float speed = 100f * Settings.scale;
            final HashMap<AbstractMonster, Boolean> hitMap = new HashMap<>();
            final HashMap<AbstractMonster, Boolean> targetToTheRight = new HashMap<>();
            final boolean hasThorns = p.hasPower(ThornsPower.POWER_ID);
            final boolean flipped = p.flipHorizontal; //TODO actually change the animation if flipped
            boolean firstPass = true;
            boolean firstPhase = true;
            boolean secondPhase = false;
            float dx;
            AbstractPower thornsPower;
            @Override
            public void update() {
                if (firstPass) {
                    firstPass = false;
                    for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                        if (!aM.isDeadOrEscaped()) {
                            hitMap.put(aM, false);
                            targetToTheRight.put(aM, aM.hb.cX >= p.hb.cX);
                        }
                    }
                    if (hasThorns) {
                        thornsPower = p.getPower(ThornsPower.POWER_ID);
                    }
                    if (flipped) {
                        p.flipHorizontal = false;
                    }
                }
                if (firstPhase) {
                    dx = speed;
                    p.drawX += dx;
                    p.hb.move(p.hb.cX+dx,p.hb.cY);
                    for (AbstractMonster aM : hitMap.keySet()) {
                        if (!hitMap.get(aM) && targetToTheRight.get(aM) && p.hb.cX >= aM.hb.cX) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.BLUNT_HEAVY));
                            aM.damage(new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], DamageInfo.DamageType.NORMAL));
                            hitMap.put(aM, true);
                            if (hasThorns) {
                                AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                                aM.damage(new DamageInfo(p, thornsPower.amount, DamageInfo.DamageType.THORNS));
                                thornsPower.flash();
                            }
                        }
                    }
                    if (p.drawX > Settings.WIDTH + (2 * speed)) {
                        firstPhase = false;
                        secondPhase = true;
                        p.drawX -= (Settings.WIDTH + (4 * speed));
                        p.hb.move(p.hb.cX-(Settings.WIDTH + (4 * speed)),p.hb.cY);
                    }
                }
                if (secondPhase) {
                    dx = limitMovement(speed, xB-p.drawX);
                    p.drawX += dx;
                    p.hb.move(p.hb.cX+dx,p.hb.cY);
                    for (AbstractMonster aM : hitMap.keySet()) {
                        if (!hitMap.get(aM) && !targetToTheRight.get(aM) && p.hb.cX >= aM.hb.cX) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.BLUNT_HEAVY));
                            aM.damage(new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], DamageInfo.DamageType.NORMAL));
                            hitMap.put(aM, true);
                            if (hasThorns) {
                                AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                                aM.damage(new DamageInfo(p, thornsPower.amount, DamageInfo.DamageType.THORNS));
                                thornsPower.flash();
                            }
                        }
                    }
                    if (p.drawX == xB) {
                        secondPhase = false;
                        isDone = true;
                    }
                }
                if (isDone) {
                    if (flipped) {
                        p.flipHorizontal = true;
                    }
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

    private ArrayList<AbstractMonster> findCollisions(AbstractPlayer p, AbstractMonster m) {
        Hitbox collisionCheck = new Hitbox(p.hb.x, p.hb.y, p.hb.width, p.hb.height);
        float checkSpeed = 10f;
        ArrayList<AbstractMonster> hits = new ArrayList<>();
        Vector2 tmp = new Vector2(m.hb.cX - p.hb.cX, m.hb.cY - p.hb.cY);
        if (tmp.len() == 0) {
            tmp.set(1, 0);
        }
        tmp.nor();
        while (0 < collisionCheck.cX && collisionCheck.cX < Settings.WIDTH && 0 < collisionCheck.cY && collisionCheck.cY < Settings.HEIGHT) {
            collisionCheck.move(collisionCheck.cX + tmp.x*checkSpeed, collisionCheck.cY + tmp.y*checkSpeed);
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    if (!hits.contains(aM) && aM.hb.intersects(collisionCheck)) {
                        hits.add(aM);
                    }
                }
            }
        }
        return hits;
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
