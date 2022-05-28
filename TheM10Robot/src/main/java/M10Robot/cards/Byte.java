package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class Byte extends AbstractSwappableCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Byte.class.getSimpleName());
    public static final String IMG = makeCardPath("Byte2.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 8;
    private static final int ORBS = 2;
    private static final int UPGRADE_PLUS_ORBS = 1;

    protected static ArrayList<TooltipInfo> toolTips;

    // /STAT DECLARATION/
    public Byte() {
        this(null);
    }

    public Byte(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = ORBS;
        info = baseInfo = 0;
        showEvokeValue = true;
        if (linkedCard == null) {
            setLinkedCard(new Nibble(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (toolTips == null) {
            toolTips = new ArrayList<>();
            toolTips.add(new TooltipInfo(TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]), GameDictionary.keywords.get(GameDictionary.BLOCK.NAMES[0])));
        }
        return toolTips;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        } else {
            this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
        this.addToBot(new MultichannelAction(new BitOrb(), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_ORBS);
            upgradeInfo(1);
            isMultiDamage = true;
            target = CardTarget.ALL_ENEMY;
            initializeDescription();
            super.upgrade();
        }
    }
}
