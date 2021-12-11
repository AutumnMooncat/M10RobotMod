package M10Robot.cutCards.boosters;

import M10Robot.M10RobotMod;
import M10Robot.cutCards.modifiers.AbstractBoosterModifier;
import M10Robot.cutCards.modifiers.TempBlockModifier;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import static M10Robot.M10RobotMod.makeCardPath;

public class ScrapBooster extends AbstractBoosterCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ScrapBooster.class.getSimpleName());
    public static final String IMG = makeCardPath("ScrapBooster.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    // /STAT DECLARATION/


    public ScrapBooster() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public Predicate<AbstractCard> getFilter() {
        return hasBlockValue;
    }

    @Override
    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
        return new ArrayList<>(Collections.singletonList(new TempBlockModifier(magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
