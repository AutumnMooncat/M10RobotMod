package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static M10Robot.M10RobotMod.makeCardPath;

public class SpikyPlating extends AbstractModuleCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SpikyPlating.class.getSimpleName());
    public static final String IMG = makeCardPath("SpikyPlating.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int THORNS = 3;
    private static final int UPGRADE_PLUS_THORNS = 2;

    // /STAT DECLARATION/


    public SpikyPlating() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = THORNS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_THORNS);
            initializeDescription();
        }
    }

    @Override
    public void onEquip() {
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, magicNumber)));
    }

    @Override
    public void onRemove() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, ThornsPower.POWER_ID, magicNumber));
    }
}
