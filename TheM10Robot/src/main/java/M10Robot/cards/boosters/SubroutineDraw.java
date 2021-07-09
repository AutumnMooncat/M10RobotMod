package M10Robot.cards.boosters;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.cardModifiers.DrawCardEffect;
import M10Robot.cardModifiers.GainBlockEffect;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import static M10Robot.M10RobotMod.makeCardPath;

public class SubroutineDraw extends AbstractBoosterCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SubroutineDraw.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public SubroutineDraw() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public Predicate<AbstractCard> getFilter() {
        return isPlayable;
    }

    @Override
    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
        return new ArrayList<>(Collections.singletonList(new DrawCardEffect(this)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
