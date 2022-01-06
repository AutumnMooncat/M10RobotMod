package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.UnloadAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static M10Robot.M10RobotMod.makeCardPath;

public class Filter extends AbstractDynamicCard implements BranchingUpgradesCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Filter.class.getSimpleName());
    public static final String IMG = makeCardPath("Filter.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public Filter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int nonAttacks = (int) p.hand.group.stream().filter(c -> c.type != CardType.ATTACK).count();
                this.addToTop(new GainEnergyAction(nonAttacks));
                this.isDone = true;
            }
        });
        this.addToBot(new UnloadAction(p));
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
        upgradeBaseCost(UPGRADE_COST);
    }

    public void branchUpgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_DRAW);
    }
}
