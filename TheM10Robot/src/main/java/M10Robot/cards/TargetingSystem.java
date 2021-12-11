package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.powers.TargetingSystemPower;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class TargetingSystem extends AbstractDynamicCard implements BranchingUpgradesCard, ModularDescription {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(TargetingSystem.class.getSimpleName());
    public static final String IMG = makeCardPath("TargetingSystem.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int LOCK = 1;
    private static final int UPGRADE_PLUS_LOCK = 1;

    // /STAT DECLARATION/


    public TargetingSystem() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = LOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new TargetingSystemPower(p, magicNumber)));
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
        upgradeMagicNumber(UPGRADE_PLUS_LOCK);
    }

    public void branchUpgrade() {
        this.isInnate = true;
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                if (isInnate) {
                    rawDescription = EXTENDED_DESCRIPTION[1];
                } else {
                    rawDescription = EXTENDED_DESCRIPTION[0];
                }
            } else {
                if (isInnate) {
                    rawDescription = UPGRADE_DESCRIPTION;
                } else {
                    rawDescription = DESCRIPTION;
                }
            }
        }
    }
}
