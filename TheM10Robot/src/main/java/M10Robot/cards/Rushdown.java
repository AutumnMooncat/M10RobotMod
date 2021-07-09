package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class Rushdown extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Rushdown.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

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

    public Rushdown() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        float xB = p.drawX;
        float yB = p.drawY;
        float speed = 150f * Settings.scale;
        boolean hasThorns = p.hasPower(ThornsPower.POWER_ID);
        ArrayList<AbstractMonster> hits = findCollisions(p, m);
        Vector2 tmp = new Vector2(m.hb.cX - p.hb.cX, m.hb.cY - p.hb.cY);
        if (tmp.len() == 0) {
            tmp.set(1, 0);
        }
        tmp.nor();
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.drawX += tmp.x*speed;
                p.drawY += tmp.y*speed;
                if (!(0 < p.drawX && p.drawX < Settings.WIDTH && 0 < p.drawY && p.drawY < Settings.HEIGHT)) {
                    this.isDone = true;
                    p.drawX = -2*xB;
                    p.drawY = yB;
                }
            }
        });

        for (AbstractMonster aM : hits) {
            this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            if (hasThorns) {
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        p.getPower(ThornsPower.POWER_ID).onAttacked(new DamageInfo(aM, 0, DamageInfo.DamageType.NORMAL), 0);
                        this.isDone = true;
                    }
                });
            }
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.drawX += Math.min(speed, xB-p.drawX);
                p.drawY = yB;
                if (p.drawX == xB) {
                    this.isDone = true;
                }
            }
        });
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
