package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import M10Robot.powers.WideAnglePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class WideAngle extends AbstractDynamicCard implements ModularDescription {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(WideAngle.class.getSimpleName());
    public static final String IMG = makeCardPath("WideAngle.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    private static final int ORBS = 2;
    private static final int UPGRADE_PLUS_ORBS = 1;

    // /STAT DECLARATION/


    public WideAngle() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        secondMagicNumber = baseSecondMagicNumber = ORBS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new WideAnglePower(p, magicNumber)));
        this.addToBot(new MultichannelAction(new BitOrb(), secondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_ORBS);
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
}
