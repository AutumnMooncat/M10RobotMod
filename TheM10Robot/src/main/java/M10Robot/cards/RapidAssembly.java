package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class RapidAssembly extends AbstractSwappableCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(RapidAssembly.class.getSimpleName());
    public static final String IMG = makeCardPath("RapidAssembly.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 1;
    private static final int E = 1;

    // /STAT DECLARATION/

    public RapidAssembly() {
        this(null);
    }

    public RapidAssembly(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
        secondMagicNumber = baseSecondMagicNumber = E;
        this.exhaust = true;
        if (linkedCard == null) {
            setLinkedCard(new PerpetualAssembly(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new GainEnergyAction(secondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
            super.upgrade();
        }
    }
}
