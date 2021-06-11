package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.SwapCardsAction;
import M10Robot.cards.abstractCards.AbstractClickableCard;
import M10Robot.cards.interfaces.SwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class MultiTool2 extends AbstractClickableCard implements UniqueCard, SwappableCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(MultiTool2.class.getSimpleName());
    public static final String IMG = makeCardPath("MultiTool2.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    // /STAT DECLARATION/


    public MultiTool2() {
        this(true);
    }

    public MultiTool2(boolean needsPreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        if (needsPreview) {
            this.cardsToPreview = new MultiTool(false);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            if (cardsToPreview != null) cardsToPreview.upgrade();
        }
    }

    @Override
    public void onRightClick() {
        CardCrawlGame.sound.play("CARD_UPGRADE", 0.1F);
        this.addToTop(new SwapCardsAction(this, cardsToPreview));
    }
}
