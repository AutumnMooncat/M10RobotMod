package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.tokenCards.Nibble;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class Byte extends AbstractDynamicCard {

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
    private static final int CARDS = 2;
    private static final int ORBS = 1;
    private static final int UPGRADE_PLUS_ORBS = 1;

    // /STAT DECLARATION/

    public Byte() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = CARDS;
        magicNumber = baseMagicNumber = ORBS;
        showEvokeValue = true;
        cardsToPreview = new Nibble();
        baseInfo = info = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new MultichannelAction(new BitOrb(), magicNumber));
        if (info == 0) {
            this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview, secondMagicNumber, true, true));
        } else {
            this.addToBot(new MakeTempCardInHandAction(cardsToPreview, secondMagicNumber));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_ORBS);
            upgradeInfo(1);
            initializeDescription();
        }
    }
}
