package M10Robot.cutStuff.boosters;

import M10Robot.M10RobotMod;
import M10Robot.cutStuff.modifiers.AbstractBoosterModifier;
import M10Robot.cutStuff.modifiers.TempMagicNumberModifier;
import M10Robot.cutStuff.AbstractBoosterCard;
import M10Robot.characters.M10Robot;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

import static M10Robot.M10RobotMod.makeCardPath;

public class MagicTinkering extends AbstractBoosterCard implements BranchingUpgradesCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(MagicTinkering.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicTinkering.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int EFFECT = 1;
    private static final int UPGRADE_PLUS_EFFECT = 1;

    // /STAT DECLARATION/


    public MagicTinkering() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public Predicate<AbstractCard> getFilter() {
        return hasMagicValue;
    }

    @Override
    public ArrayList<AbstractBoosterModifier> getBoosterModifiers() {
        return new ArrayList<>(Collections.singletonList(new TempMagicNumberModifier(magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (isBranchUpgrade()) {
                branchUpgrade();
            } else {
                baseUpgrade();
            }
            initializeDescription();
        }
    }

    public void baseUpgrade() {
        upgradeMagicNumber(UPGRADE_PLUS_EFFECT);
    }

    public void branchUpgrade() {
        this.selfRetain = true;
        rawDescription = UPGRADE_DESCRIPTION;
    }
}
