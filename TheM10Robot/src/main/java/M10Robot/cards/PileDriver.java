package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.PileDriverAction;
import M10Robot.cardModifiers.SpikyModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.patches.RestorePositionPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
        this.addToBot(new PileDriverAction(p, m, multiDamage, damageTypeForTurn));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.removeBackUp(p);
                this.isDone = true;
            }
        });
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
