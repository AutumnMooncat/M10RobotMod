package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.patches.LockOrbAnimationPatches;
import basemod.abstracts.CustomOrb;
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

import static M10Robot.M10RobotMod.makeCardPath;

public class Salvo extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Salvo.class.getSimpleName());
    public static final String IMG = makeCardPath("Salvo.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    // /STAT DECLARATION/

    public Salvo() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            boolean firstPass = true;
            final HashMap<AbstractOrb, Vector2> vecMap = new HashMap<>();
            final HashMap<AbstractOrb, Vector2> offsetMap = new HashMap<>();
            final HashMap<AbstractOrb, Float> backupX = new HashMap<>();
            final HashMap<AbstractOrb, Float> backupY = new HashMap<>();
            final HashMap<AbstractOrb, Boolean> isDoneMap = new HashMap<>();
            final float mainSpeed = 75 * Settings.scale;
            final float offsetSpeed = 25 * Settings.scale;
            float dx,dy;
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
                            m.damage(new DamageInfo(p, damage, damageTypeForTurn));
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
                    while (!(p.orbs.get(0) instanceof EmptyOrbSlot)) {
                        p.removeNextOrb();
                    }
                }
            }
        });

        //Old effect, throws orbs 1 at a time.
        /*
        //For each orb...
        for (AbstractOrb o : p.orbs) {
            if (!(o instanceof EmptyOrbSlot)) {
                //Stop the Orb from trying to fix its animation position
                LockOrbAnimationPatches.StopAnimatingField.stopAnimating.set(o, true);
                //grab the vector
                float speed = 100f * Settings.scale;
                float backupX = o.cX;
                float backupY = o.cY;
                Vector2 aim = new Vector2(m.hb.cX-o.cX, m.hb.cY-o.cY).nor();
                //move the orb until it collides
                this.addToBot(new AbstractGameAction() {
                    float dx,dy;
                    @Override
                    public void update() {
                        dx = limitMovement(speed*aim.x, m.hb.cX-o.cX);
                        dy = limitMovement(speed*aim.y, m.hb.cY-o.cY);
                        o.cX += dx;
                        o.cY += dy;
                        o.hb.move(o.hb.cX+dx,o.hb.cY+dy);
                        if (o.cX == m.hb.cX && o.cY == m.hb.cY) {
                            this.isDone = true;
                        }
                    }
                });

                //Deal damage to appropriate targets and play sfx/vfx
                this.addToBot(new SFXAction("GHOST_ORB_IGNITE_2"));
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                        this.isDone = true;
                    }
                });
                this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),true));

                //remove the orb by replacing it with an empty slot
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        o.cX = backupX;
                        o.cY = backupY;
                        LockOrbAnimationPatches.StopAnimatingField.stopAnimating.set(o, false);
                        p.removeNextOrb();
                        this.isDone = true;
                    }
                });
            }
        }*/
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
