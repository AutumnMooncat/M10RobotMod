package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.EMPPower;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static M10Robot.M10RobotMod.makeCardPath;

public class Disable extends AbstractDynamicCard implements BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Disable.class.getSimpleName());
    public static final String IMG = makeCardPath("Disable.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int BASE = 1;
    private static final int EFFECT = 2;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/


    public Disable() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        secondMagicNumber = baseSecondMagicNumber = BASE;
        info = baseInfo = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new EMPPower(p, magicNumber)));
        this.addToBot(new ApplyPowerAction(p, p, new WeakPower(p, secondMagicNumber, false)));
        if (upgraded && info > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, secondMagicNumber, false)));
        }
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
        upgradeBaseCost(UPGRADE_COST);
    }

    public void branchUpgrade() {
        upgradeInfo(UPGRADE_PLUS_EFFECT);
    }
}
