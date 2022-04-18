package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.SearchlightOrb;
import M10Robot.powers.SpikesPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Surveillance extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Surveillance.class.getSimpleName());
    public static final String IMG = makeCardPath("Plan.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int ORBS = 1;
    private static final int SPIKES = 3;

    // /STAT DECLARATION/


    public Surveillance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ORBS;
        secondMagicNumber = baseSecondMagicNumber = SPIKES;
        showEvokeValue = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MultichannelAction(new SearchlightOrb(), magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new SpikesPower(p, secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
