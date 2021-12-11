package M10Robot.cutStuff.boosters;

import M10Robot.M10RobotMod;
import M10Robot.cutStuff.modifiers.AbstractBoosterModifier;
import M10Robot.cutStuff.modifiers.TempRefundModifier;
import M10Robot.cutStuff.AbstractBoosterCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import static M10Robot.M10RobotMod.makeCardPath;

public class SaleTag extends AbstractBoosterCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SaleTag.class.getSimpleName());
    public static final String IMG = makeCardPath("SaleTag.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int REFUND = 1;

    // /STAT DECLARATION/


    public SaleTag() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = REFUND;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public Predicate<AbstractCard> getFilter() {
        return nonZeroCost;
    }

    @Override
    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
        return new ArrayList<>(Collections.singletonList(new TempRefundModifier(magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
