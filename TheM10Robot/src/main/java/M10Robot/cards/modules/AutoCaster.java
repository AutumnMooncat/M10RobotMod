package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class AutoCaster extends AbstractModuleCard implements ModularDescription {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(AutoCaster.class.getSimpleName());
    public static final String IMG = makeCardPath("AutoCaster.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int EVOKES = 1;
    private static final int UPGRADE_PLUS_EVOKES = 1;

    // /STAT DECLARATION/


    public AutoCaster() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EVOKES;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EVOKES);
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        }
    }

    @Override
    public void onEquip() {}

    @Override
    public void onRemove() {}
}
