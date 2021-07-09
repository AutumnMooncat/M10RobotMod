package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

import static M10Robot.M10RobotMod.makeCardPath;

public class SlotConversion extends AbstractModuleCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SlotConversion.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int SLOTS = 2;
    private static final int UPGRADE_PLUS_SLOTS = 1;

    private int slotsActuallyAdded;

    // /STAT DECLARATION/


    public SlotConversion() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = SLOTS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SLOTS);
            initializeDescription();
        }
    }

    @Override
    public void onEquip() {
        slotsActuallyAdded = Math.min(10 - AbstractDungeon.player.maxOrbs, magicNumber);
        this.addToTop(new IncreaseMaxOrbAction(slotsActuallyAdded));
    }

    @Override
    public void onRemove() {
        this.addToTop(new DecreaseMaxOrbAction(slotsActuallyAdded));
    }
}
