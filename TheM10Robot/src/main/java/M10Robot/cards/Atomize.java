package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BombOrb;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import static M10Robot.M10RobotMod.makeCardPath;

public class Atomize extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Atomize.class.getSimpleName());
    public static final String IMG = makeCardPath("Atomize.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int ORBS = 2;
    //private static final int UPGRADE_PLUS_ORBS = 1;

    // /STAT DECLARATION/


    public Atomize() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ORBS;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < magicNumber ; i++) {
            this.addToBot(new ChannelAction(new BombOrb()));
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractOrb o : p.orbs) {
                    if (o instanceof BombOrb) {
                        this.addToTop(new EvokeSpecificOrbAction(o));
                    }
                }
                this.isDone = true;
            }
        });
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            //upgradeMagicNumber(UPGRADE_PLUS_ORBS);
            initializeDescription();
        }
    }
}