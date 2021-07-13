package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static M10Robot.M10RobotMod.makeCardPath;

public class RapidAssembly extends AbstractDynamicCard implements ModularDescription, BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(RapidAssembly.class.getSimpleName());
    public static final String IMG = makeCardPath("RapidAssembly.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int DRAW = 1;

    // /STAT DECLARATION/


    public RapidAssembly() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
        this.exhaust = true;
        initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        AbstractCard card;
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.addAll(AbstractDungeon.commonCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
        cards.addAll(AbstractDungeon.uncommonCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
        cards.addAll(AbstractDungeon.rareCardPool.group.stream().filter(c -> c instanceof AbstractBoosterCard).collect(Collectors.toCollection(ArrayList::new)));
        card = cards.get(AbstractDungeon.cardRandomRng.random(cards.size()-1));
        this.addToBot(new MakeTempCardInHandAction(card));
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
        this.exhaust = false;
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                if (!exhaust) {
                    rawDescription = EXTENDED_DESCRIPTION[1];
                } else {
                    rawDescription = EXTENDED_DESCRIPTION[0];
                }
            } else {
                if (!exhaust) {
                    rawDescription = UPGRADE_DESCRIPTION;
                } else {
                    rawDescription = DESCRIPTION;
                }
            }
        }
    }
}
