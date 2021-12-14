package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class BatteryPack extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(BatteryPack.class.getSimpleName());
    public static final String IMG = makeCardPath("BatteryPack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int MAX_CHARGES = 3;
    private static final int UPGRADE_PLUS_MAX_CHARGES = 2;

    // /STAT DECLARATION/


    public BatteryPack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAX_CHARGES;
        secondMagicNumber = baseSecondMagicNumber = 0;
        selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (secondMagicNumber > 0) {
            this.addToBot(new GainEnergyAction(secondMagicNumber));
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    secondMagicNumber = baseSecondMagicNumber = 0;
                    initializeDescription();
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void onRetained() {
        if (secondMagicNumber < magicNumber) {
            CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
            upgradeSecondMagicNumber(1);
            superFlash();
            initializeDescription();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAX_CHARGES);
            initializeDescription();
        }
    }
}
