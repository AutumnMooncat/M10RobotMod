package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.TurboPower;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;

import static M10Robot.M10RobotMod.makeCardPath;

public class Turbo extends AbstractDynamicCard implements BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Turbo.class.getSimpleName());
    public static final String IMG = makeCardPath("Turbo.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int UPGRADE_COST = 0;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;
    private static final int NRG = 1;
    private static final int UPGRADE_PLUS_NRG = 1;

    // /STAT DECLARATION/


    public Turbo() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
        secondMagicNumber = baseSecondMagicNumber = NRG;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new EnergizedBluePower(p, secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
            initializeDescription();
        }
    }

    public void baseUpgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_DRAW);
    }

    public void branchUpgrade() {
        upgradeSecondMagicNumber(UPGRADE_PLUS_NRG);
    }
}
