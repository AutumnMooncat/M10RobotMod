package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RecoilPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class Assault extends AbstractDynamicCard implements UniqueCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Assault.class.getSimpleName());
    public static final String IMG = makeCardPath("Assault.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DRAW = 1;

    // /STAT DECLARATION/

    public Assault() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = DRAW;
        isMultiDamage = true;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                this.addToBot(new VFXAction(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY), 0.1F));
            }
        }
        this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new DrawCardAction(p, magicNumber));
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
