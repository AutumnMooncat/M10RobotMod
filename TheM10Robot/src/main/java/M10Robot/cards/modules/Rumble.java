package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Rumble extends AbstractModuleCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Rumble.class.getSimpleName());
    public static final String IMG = makeCardPath("Rumble.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int STRENGTH = 1;
    private static final int UPGRADE_PLUS_STRENGTH = 1;

    // /STAT DECLARATION/


    public Rumble() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        //This should ensure the card can take any booster
        magicNumber = baseMagicNumber = STRENGTH;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_STRENGTH);
            initializeDescription();
        }
    }

    @Override
    public void onEquip() {}

    @Override
    public void onRemove() {}
}
