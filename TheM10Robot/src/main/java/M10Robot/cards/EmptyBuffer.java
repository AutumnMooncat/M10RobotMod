package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
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

    // /STAT DECLARATION/


    public EmptyBuffer() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb o : p.orbs) {
            if (!(o instanceof EmptyOrbSlot)) {
                if (upgraded) {
                    this.addToTop(new ChannelAction(o, false));
                }
                this.addToTop(new EvokeOrbAction(1));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}