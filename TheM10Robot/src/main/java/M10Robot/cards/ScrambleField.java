package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.NegativePrimaryEffect;
import M10Robot.characters.M10Robot;
import M10Robot.powers.ScrambleFieldPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class ScrambleField extends AbstractDynamicCard implements NegativePrimaryEffect {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ScrambleField.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int EFFECT = 7;
    private static final int UPGRADE_PLUS_EFFECT = -2;
    private static final int OFFSET = 1;
    // /STAT DECLARATION/


    public ScrambleField() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = OFFSET;
        secondMagicNumber = baseSecondMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new ScrambleFieldPower(m, secondMagicNumber)));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        doMagicNumberShenanigans();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        doMagicNumberShenanigans();
    }

    private void doMagicNumberShenanigans() {
        if (AbstractDungeon.player != null && !AbstractDungeon.isScreenUp) {
            int delta = magicNumber - OFFSET;
            secondMagicNumber = EFFECT + (upgraded ? UPGRADE_PLUS_EFFECT : 0);
            baseSecondMagicNumber = EFFECT + (upgraded ? UPGRADE_PLUS_EFFECT : 0);
            if (delta > 0) {
                //time for some shenanigans
                secondMagicNumber = Math.max(secondMagicNumber-delta,0);
                baseSecondMagicNumber = Math.max(baseSecondMagicNumber-delta,0);
            }
            isSecondMagicNumberModified = secondMagicNumber != baseSecondMagicNumber;
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_EFFECT);
            initializeDescription();
        }
    }

    @Override
    public boolean reboundOnNegative() {
        return false;
    }
}
