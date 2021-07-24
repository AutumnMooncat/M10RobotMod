package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.cards.interfaces.NegativePrimaryEffect;
import M10Robot.characters.M10Robot;
import M10Robot.patches.RestorePositionPatches;
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

import static M10Robot.M10RobotMod.makeCardPath;

public class SelfDestruction extends AbstractDynamicCard implements ModularDescription, NegativePrimaryEffect {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SelfDestruction.class.getSimpleName());
    public static final String IMG = makeCardPath("SelfDestruction.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 10;
    private static final int HP_LOSS = 5;
    private static final int OFFSET = 1;

    private boolean invertedEffect;

    // /STAT DECLARATION/

    public SelfDestruction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = OFFSET;
        secondMagicNumber = baseSecondMagicNumber = HP_LOSS;
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
            final float xBb = p.drawX;
            final float yBd = p.drawY;
            final float xBh = p.hb.cX;
            final float yBh = p.hb.cY;
            final float tx = m.hb.cX;
            final float ty = m.hb.cY;
            final float speed = 50f * Settings.scale;
            final float damageFalloffIncrements = 80f * Settings.scale;
            float dst1;
            Vector2 tmp = new Vector2(tx - p.hb.cX, ty - p.hb.cY);
            float currentSpeed = speed;
            float dx, dy;
            boolean firstPass = true;
            int actionPhase;
            float waitTimer = 0f;
            final float pause = 0.1f;
            final ArrayList<AbstractMonster> hits = new ArrayList<>();
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
                    if (secondMagicNumber != 0) {
                        if (invertedEffect) {
                            p.heal(secondMagicNumber);
                        } else {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                            p.damage(new DamageInfo(p, secondMagicNumber, DamageInfo.DamageType.HP_LOSS));
                        }
                    }
                    AbstractDungeon.effectsQueue.add(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.RED.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC));
                    //advance the action
                    actionPhase++;
                    //Set up the hits array
                    for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                        if (!aM.isDeadOrEscaped()) {
                            hits.add(aM);
                        }
                    }
                    hits.sort((rhs, lhs) -> Float.compare(new Vector2(rhs.hb.cX - p.hb.cX, rhs.hb.cY - p.hb.cY ).len(), new Vector2(lhs.hb.cX - p.hb.cX, lhs.hb.cY - p.hb.cY).len()));
                } else if (actionPhase == 2) {
                    //Damage the enemies, but not all at once
                    if (!hits.isEmpty()) {
                        AbstractMonster aM = hits.get(0);
                        float dst = new Vector2(aM.hb.cX - p.hb.cX, aM.hb.cY - p.hb.cY).len();
                        int falloff = (int) Math.ceil(dst/damageFalloffIncrements) - 1;
                        int damage = multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)] - falloff;
                        if (damage > 0) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AbstractGameAction.AttackEffect.FIRE, true));
                            aM.damage(new DamageInfo(p, damage, damageTypeForTurn));
                        }
                        hits.remove(0);
                        //use a timer to slow things down so it isn't instant
                        waitTimer = pause;
                    } else {
                        //We hit everyone, advance the action
                        p.drawX = -2*xBb;
                        p.drawY = yBd;
                        p.tint.changeColor(Color.WHITE.cpy());
                        p.hb.move(xBh, yBh);
                        actionPhase++;
                    }
                } else if (actionPhase == 3) {
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
        });

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.removeBackUp(p);
                this.isDone = true;
            }
        });
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        doMagicNumberShenanigans();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        doMagicNumberShenanigans();
    }

    private void doMagicNumberShenanigans() {
        if (AbstractDungeon.player != null && !AbstractDungeon.isScreenUp) {
            int delta = magicNumber - OFFSET;
            secondMagicNumber = HP_LOSS;
            baseSecondMagicNumber = HP_LOSS;
            if (delta > 0) {
                //time for some shenanigans
                secondMagicNumber -= delta;
                baseSecondMagicNumber -= delta;
                invertedEffect = secondMagicNumber < 0;
                if (invertedEffect) {
                    secondMagicNumber = -secondMagicNumber;
                    baseSecondMagicNumber = -baseSecondMagicNumber;
                }
            }
            isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        }
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

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (invertedEffect) {
                rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
            } else {
                if (magicNumber == (HP_LOSS+OFFSET)) {
                    rawDescription = DESCRIPTION;
                } else {
                    rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
                }
            }
        }
    }

    @Override
    public boolean reboundOnNegative() {
        return true;
    }
}
