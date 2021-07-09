package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.FasterLoseHPAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.cards.interfaces.NegativePrimaryEffect;
import M10Robot.characters.M10Robot;
import M10Robot.vfx.BurnToAshEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class SelfDestruction extends AbstractDynamicCard implements ModularDescription, NegativePrimaryEffect {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SelfDestruction.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

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
        this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
        this.addToBot(new VFXAction(new IntenseZoomEffect(Settings.WIDTH/2f, Settings.HEIGHT/2f, false)));
        p.tint.changeColor(Color.ORANGE.cpy());
        float xBb = p.drawX;
        float yBd = p.drawY;
        float xBh = p.hb.cX;
        float yBh = p.hb.cY;
        float tx = m.hb.cX;
        float ty = m.hb.cY;
        final float speed = 50f * Settings.scale;
        float dst1;
        float damageFalloffIncrements = 80f * Settings.scale;
        Vector2 tmp = new Vector2(tx - p.hb.cX, ty - p.hb.cY);
        dst1 = tmp.len();
        if (tmp.len() == 0) {
            tmp.set(1, 0);
        }
        tmp.nor();
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
                float dst2 = new Vector2(tx - p.hb.cX, ty - p.hb.cY).len();
                currentSpeed = speed*(dst2/dst1);
                if (currentSpeed <= 3f || dst2 < damageFalloffIncrements) {
                    this.isDone = true;
                }
            }
        });

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(p.hb.cX, p.hb.cY));
                AbstractDungeon.effectsQueue.add(new BurnToAshEffect(p.hb.cX, p.hb.cY));
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                this.isDone = true;
            }
        });

        //this.addToBot(new SFXAction("ORB_LIGHTNING_PASSIVE"));
        this.addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        //this.addToBot(new SFXAction("GHOST_ORB_IGNITE_1"));
        this.addToBot(new SFXAction("GHOST_ORB_IGNITE_2"));
        this.addToBot(new SFXAction("WATCHER_HEART_PUNCH"));
        //this.addToBot(new SFXAction("ATTACK_FIRE"));
        if (secondMagicNumber != 0) {
            if (invertedEffect) {
                this.addToBot(new HealAction(p, p, secondMagicNumber));
            } else {
                this.addToBot(new FasterLoseHPAction(p, p, secondMagicNumber, AbstractGameAction.AttackEffect.FIRE));
            }
        }
        this.addToBot(new VFXAction(p, new ShockWaveEffect(tx, ty, Color.RED.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC), 0.0f));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractMonster> hits = new ArrayList<>();
                for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                    if (!aM.isDeadOrEscaped()) {
                        hits.add(aM);
                    }
                }
                hits.sort((lhs, rhs) -> Float.compare(new Vector2(rhs.hb.cX - p.hb.cX, rhs.hb.cY - p.hb.cY ).len(), new Vector2(lhs.hb.cX - p.hb.cX, lhs.hb.cY - p.hb.cY).len()));
                //Collections.reverse(hits);
                for (AbstractMonster aM : hits) {
                    float dst = new Vector2(aM.hb.cX - p.hb.cX, aM.hb.cY - p.hb.cY).len();
                    int falloff = (int) Math.ceil(dst/damageFalloffIncrements) - 1;
                    int damage = multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)] - falloff;
                    if (damage > 0) {
                        this.addToTop(new DamageAction(aM, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true, true));
                    }
                }
                this.isDone = true;
            }
        });

        this.addToBot(new AbstractGameAction() {
            boolean firstPass = true;
            @Override
            public void update() {
                if (firstPass) {
                    p.drawX = -2*xBb;
                    p.drawY = yBd;
                    firstPass = false;
                    p.tint.changeColor(Color.WHITE.cpy());
                    p.hb.move(xBh, yBh);
                }
                p.drawX += Math.min(speed, xBb-p.drawX);
                p.drawY = yBd;
                if (p.drawX == xBb) {
                    this.isDone = true;
                }
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
