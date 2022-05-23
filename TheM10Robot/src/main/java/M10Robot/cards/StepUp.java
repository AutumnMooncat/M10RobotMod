package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.ExtractAction;
import M10Robot.actions.OverclockCardAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class StepUp extends AbstractDynamicCard implements CannotOverclock {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(StepUp.class.getSimpleName());
    public static final String IMG = makeCardPath("StepUp.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int EXTRACT = 1;
    private static final int UPGRADE_PLUS_EXTRACT = 1;
    private static final int OVERCLOCK = 1;
    private static final int UPGRADE_PLUS_OVERCLOCK = 1;

    // /STAT DECLARATION/


    public StepUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = OVERCLOCK;
        secondMagicNumber = baseSecondMagicNumber = EXTRACT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ExtractAction(secondMagicNumber, false, card -> {
            StepUp.this.addToTop(new OverclockCardAction(card, magicNumber));
        }));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_OVERCLOCK);
            //this.selfRetain = true;
            initializeDescription();
        }
    }
}