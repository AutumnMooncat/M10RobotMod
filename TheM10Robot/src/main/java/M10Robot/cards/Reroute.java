package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.ExtractAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.OnOverclockCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Reroute extends AbstractDynamicCard implements OnOverclockCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Reroute.class.getSimpleName());
    public static final String IMG = makeCardPath("Reroute.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int EXTRACT = 1;
    private static final int BOOST = 4;
    private static final int UPGRADE_PLUS_BOOST = 2;

    // /STAT DECLARATION/


    public Reroute() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = EXTRACT;
        secondMagicNumber = baseSecondMagicNumber = BOOST;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.addToBot(new ExtractAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeSecondMagicNumber(UPGRADE_PLUS_BOOST);
            initializeDescription();
        }
    }

    @Override
    public void onOverclock(int amount) {
        upgradeDamage(secondMagicNumber);
    }
}
