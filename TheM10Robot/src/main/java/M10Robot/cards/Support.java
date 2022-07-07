package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.FollowupPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class Support extends AbstractSwappableCard implements UniqueCard, CannotOverclock {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Support.class.getSimpleName());
    public static final String IMG = makeCardPath("Support.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int CARDS = 1;

    protected static ArrayList<TooltipInfo> toolTips;

    // /STAT DECLARATION/

    public Support() {
        this(null);
    }

    public Support(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CARDS;
        if (linkedCard == null) {
            setLinkedCard(new Tinker(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (toolTips == null) {
            toolTips = new ArrayList<>();
            toolTips.add(new TooltipInfo(BaseMod.getKeywordTitle(cardStrings.EXTENDED_DESCRIPTION[0]), BaseMod.getKeywordDescription(cardStrings.EXTENDED_DESCRIPTION[0])));
        }
        return toolTips;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new ApplyPowerAction(p, p, new FollowupPower(p, 1, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}
