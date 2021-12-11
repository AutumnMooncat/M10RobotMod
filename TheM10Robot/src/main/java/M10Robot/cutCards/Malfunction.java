package M10Robot.cutCards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Malfunction extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Malfunction.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int LOSS = 1;

    // /STAT DECLARATION/


    public Malfunction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        secondMagicNumber = baseSecondMagicNumber = LOSS;
        exhaust = true;
        selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LoseHPAction(p, p, secondMagicNumber));
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {}
}
