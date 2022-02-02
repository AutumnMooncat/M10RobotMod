package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.RushdownAction;
import M10Robot.cardModifiers.SpikyModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.patches.RestorePositionPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/

    public Rushdown() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        block = baseBlock = BLOCK;
        isMultiDamage = true;
        CardModifierManager.addModifier(this, new SpikyModifier());
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
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new RushdownAction(p, multiDamage, damageTypeForTurn));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.removeBackUp(p);
                this.isDone = true;
            }
        });
    }

//    private ArrayList<AbstractMonster> findCollisions(AbstractPlayer p, AbstractMonster m) {
//        Hitbox collisionCheck = new Hitbox(p.hb.x, p.hb.y, p.hb.width, p.hb.height);
//        float checkSpeed = 10f;
//        ArrayList<AbstractMonster> hits = new ArrayList<>();
//        Vector2 tmp = new Vector2(m.hb.cX - p.hb.cX, m.hb.cY - p.hb.cY);
//        if (tmp.len() == 0) {
//            tmp.set(1, 0);
//        }
//        tmp.nor();
//        while (0 < collisionCheck.cX && collisionCheck.cX < Settings.WIDTH && 0 < collisionCheck.cY && collisionCheck.cY < Settings.HEIGHT) {
//            collisionCheck.move(collisionCheck.cX + tmp.x*checkSpeed, collisionCheck.cY + tmp.y*checkSpeed);
//            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
//                if (!aM.isDeadOrEscaped()) {
//                    if (!hits.contains(aM) && aM.hb.intersects(collisionCheck)) {
//                        hits.add(aM);
//                    }
//                }
//            }
//        }
//        return hits;
//    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
