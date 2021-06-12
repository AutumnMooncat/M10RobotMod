package M10Robot.cutCards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class MalfunctionOLD extends AbstractDynamicCard implements ModularDescription {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(MalfunctionOLD.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARDS = 1;
    private static final int LOSS = 1;

    // /STAT DECLARATION/


    public MalfunctionOLD() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = LOSS;
        returnToHand = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new LoseHPAction(p, p, secondMagicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        changeDescription();
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        changeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            changeDescription();
            this.selfRetain = true;
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (magicNumber == 1) {
            if (upgraded) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        } else {
            if (upgraded) {
                rawDescription = EXTENDED_DESCRIPTION[1];
            } else {
                rawDescription = EXTENDED_DESCRIPTION[0];
            }
        }
        initializeDescription();
    }
}
