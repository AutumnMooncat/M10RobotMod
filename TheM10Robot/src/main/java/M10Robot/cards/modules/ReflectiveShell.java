package M10Robot.cards.modules;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.abstractCards.AbstractModdedCard;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RecoveryModePower;
import M10Robot.powers.ReflectiveShellPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class ReflectiveShell extends AbstractModuleCard implements ModularDescription{


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(ReflectiveShell.class.getSimpleName());
    public static final String IMG = makeCardPath("ReflectiveShell.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int EFFECT = 1;

    // /STAT DECLARATION/


    public ReflectiveShell() {
        super(ID, IMG, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EFFECT;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ReflectiveShellPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void onEquip() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ReflectiveShellPower(AbstractDungeon.player, magicNumber)));
    }

    @Override
    public void onRemove() {
        this.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, ReflectiveShellPower.POWER_ID, magicNumber));
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                if (upgraded) {
                    rawDescription = EXTENDED_DESCRIPTION[1];
                } else {
                    rawDescription = EXTENDED_DESCRIPTION[0];
                }
            } else {
                if (upgraded) {
                    rawDescription = UPGRADE_DESCRIPTION;
                } else {
                    rawDescription = DESCRIPTION;
                }
            }
        }
    }
}
