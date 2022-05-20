package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.ScrapperAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Scrapper extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Scrapper.class.getSimpleName());
    public static final String IMG = makeCardPath("Scrapper.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;
    private static final int SPIKES = 3;
    private static final int UPGRADE_PLUS_SPIKES = 2;
    private static final int SPU = 2;
    private static final int UPGRADE_PLUS_SPU = 1;

    // /STAT DECLARATION/


    public Scrapper() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SPIKES;
        secondMagicNumber = baseSecondMagicNumber = SPU;
        info = baseInfo = 0;
        showEvokeValue = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AnimateOrbAction(1));
        this.addToBot(new ScrapperAction(magicNumber, secondMagicNumber, info != 0));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBaseCost(UPGRADE_COST);
            //upgradeMagicNumber(UPGRADE_PLUS_SPIKES);
            //upgradeSecondMagicNumber(UPGRADE_PLUS_SPU);
            upgradeInfo(1);
            initializeDescription();
        }
    }
}
