package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class Nibble extends AbstractSwappableCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Nibble.class.getSimpleName());
    public static final String IMG = makeCardPath("Nibble.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int EFFECT = 4;
    private static final int MONCH = 1;
    private static final int UPGRADE_PLUS_MONCH = 1;

    protected static ArrayList<TooltipInfo> toolTips;

    // /STAT DECLARATION/

    public Nibble() {
        this(null);
    }

    public Nibble(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = EFFECT;
        baseBlock = block = EFFECT;
        info = baseInfo = 0;
        if (linkedCard == null) {
            setLinkedCard(new Byte(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (toolTips == null) {
            toolTips = new ArrayList<>();
            toolTips.add(new TooltipInfo(TipHelper.capitalize(GameDictionary.CHANNEL.NAMES[0]), GameDictionary.keywords.get(GameDictionary.CHANNEL.NAMES[0])));
            toolTips.add(new TooltipInfo(BaseMod.getKeywordTitle("m10robot:bit"), BaseMod.getKeywordDescription("m10robot:bit")));
        }
        return toolTips;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        if (info == 0) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        } else {
            this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }
        /*for (AbstractOrb o : p.orbs) {
            if (o instanceof BitOrb) {
                for (int i = 0 ; i < magicNumber ; i++) {
                    this.addToBot(new BitAttackAction((BitOrb) o, p, m));
                }
            }
        }*/
    }

    /*public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player.orbs.stream().anyMatch(o -> o instanceof BitOrb)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }*/

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MONCH);
            upgradeInfo(1);
            isMultiDamage = true;
            target = CardTarget.ALL_ENEMY;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            super.upgrade();
        }
    }
}
