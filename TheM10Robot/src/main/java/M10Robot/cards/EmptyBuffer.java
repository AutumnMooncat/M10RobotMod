package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.EvokeSpecificOrbMultipleTimesAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.defect.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import static M10Robot.M10RobotMod.makeCardPath;

public class EmptyBuffer extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(EmptyBuffer.class.getSimpleName());
    public static final String IMG = makeCardPath("EmptyBuffer.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int EVOKES = 1;
    private static final int UPGRADE_PLUS_EVOKES = 1;

    // /STAT DECLARATION/


    public EmptyBuffer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = EVOKES;
        info = baseInfo = 0;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AnimateOrbAction(p.orbs.size()));
        if (info == 1) {
            this.addToBot(new EvokeAllOrbsAction());
        } else {
            this.addToBot(new RemoveAllOrbsAction());
        }
        this.addToBot(new IncreaseMaxOrbAction(p.filledOrbCount()*magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBaseCost(UPGRADE_COST);
            //upgradeMagicNumber(UPGRADE_PLUS_EVOKES);
            upgradeInfo(1);
            showEvokeValue = true;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}