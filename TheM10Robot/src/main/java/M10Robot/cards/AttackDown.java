package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import static M10Robot.M10RobotMod.makeCardPath;

public class AttackDown extends AbstractSwappableCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(AttackDown.class.getSimpleName());
    public static final String IMG = makeCardPath("AttackDown.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int EFFECT = 2;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/

    public AttackDown() {
        this(null);
    }

    public AttackDown(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
        if (linkedCard == null) {
            setLinkedCard(new DefenseDown(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int sum = 0;
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(aM, p, new WeakPower(aM, this.magicNumber, false), this.magicNumber, true));
                if (!aM.hasPower(ArtifactPower.POWER_ID)) {
                    sum += magicNumber;
                }
            }
        }
        if (sum > 0) {
            //this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, sum), sum));
            //this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, sum), sum));
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
