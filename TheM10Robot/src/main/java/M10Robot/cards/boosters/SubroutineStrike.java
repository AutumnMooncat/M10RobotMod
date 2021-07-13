package M10Robot.cards.boosters;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.cardModifiers.DealDamageEffect;
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

public class SubroutineStrike extends AbstractBoosterCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SubroutineStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("SubroutineStrike.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    // /STAT DECLARATION/


    public SubroutineStrike() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        this.tags.add(CardTags.STRIKE);
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
        return new ArrayList<>(Collections.singletonList(new DealDamageEffect(this, 1)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
