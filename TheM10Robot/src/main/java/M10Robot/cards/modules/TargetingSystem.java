package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.characters.M10Robot;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class TargetingSystem extends AbstractModuleCard implements BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(TargetingSystem.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int LOCK = 1;
    private static final int UPGRADE_PLUS_LOCK = 1;

    // /STAT DECLARATION/


    public TargetingSystem() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = LOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

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
        rawDescription = UPGRADE_DESCRIPTION;
    }

    @Override
    public void onEquip() {}

    @Override
    public void onRemove() {}
}
