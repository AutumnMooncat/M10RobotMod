package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RepairableArmorPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class ScrapArmor extends AbstractModuleCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ScrapArmor.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int ARMOR = 4;
    private static final int UPGRADE_PLUS_ARMOR = 2;

    // /STAT DECLARATION/


    public ScrapArmor() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ARMOR;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ARMOR);
            initializeDescription();
        }
    }

    @Override
    public void onEquip() {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RepairableArmorPower(AbstractDungeon.player, magicNumber, magicNumber)));
    }

    @Override
    public void onRemove() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, RepairableArmorPower.POWER_ID, magicNumber));
    }
}
