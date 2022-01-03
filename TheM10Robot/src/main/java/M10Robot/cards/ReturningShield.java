package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static M10Robot.M10RobotMod.makeCardPath;

public class ReturningShield extends AbstractDynamicCard implements BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ReturningShield.class.getSimpleName());
    public static final String IMG = makeCardPath("ReturningShield.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public ReturningShield() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block)));
        this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
            initializeDescription();
        }
    }

    public void baseUpgrade() {
        upgradeBlock(UPGRADE_PLUS_BLOCK);
    }

    public void branchUpgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_DRAW);
    }
}
