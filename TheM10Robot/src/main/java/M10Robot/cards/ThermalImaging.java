package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

import static M10Robot.M10RobotMod.makeCardPath;

public class ThermalImaging extends AbstractSwappableCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ThermalImaging.class.getSimpleName());
    public static final String IMG = makeCardPath("ThermalImaging.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 2;
    private static final int UPGRADE_PLUS_EFFECT = 1;
    private static final int ORBS = 1;

    // /STAT DECLARATION/

    public ThermalImaging() {
        this(null);
    }

    public ThermalImaging(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        secondMagicNumber = baseSecondMagicNumber = ORBS;
        showEvokeValue = true;
        if (linkedCard == null) {
            setLinkedCard(new HeatSeekers(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new LockOnPower(m, magicNumber)));
        this.addToBot(new MultichannelAction(new BitOrb(), secondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
            super.upgrade();
        }
    }
}
