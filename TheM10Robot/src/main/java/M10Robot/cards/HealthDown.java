package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static M10Robot.M10RobotMod.makeCardPath;

public class HealthDown extends AbstractSwappableCard implements UniqueCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(HealthDown.class.getSimpleName());
    public static final String IMG = makeCardPath("HealthDown.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 7;
    private static final int UPGRADE_PLUS_EFFECT = 3;

    // /STAT DECLARATION/

    public HealthDown() {
        this(null);
    }

    public HealthDown(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        if (linkedCard == null) {
            setLinkedCard(new EvasionDown(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new LoseHPAction(m, p, magicNumber, AbstractGameAction.AttackEffect.FIRE));
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.cardID.equals(EvasionDown.ID))) {
            this.addToBot(new LoseHPAction(m, p, magicNumber, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.cardID.equals(EvasionDown.ID))) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
            super.upgrade();
        }
    }
}
