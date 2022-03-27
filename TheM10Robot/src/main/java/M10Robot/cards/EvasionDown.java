package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.EvasionDownPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class EvasionDown extends AbstractSwappableCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(EvasionDown.class.getSimpleName());
    public static final String IMG = makeCardPath("EvasionDown.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 3;
    private static final int UPGRADE_PLUS_EFFECT = 2;

    // /STAT DECLARATION/

    public EvasionDown() {
        this(null);
    }

    public EvasionDown(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        if (linkedCard == null) {
            setLinkedCard(new HealthDown(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new EvasionDownPower(m, p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
            super.upgrade();
        }
    }
}
