package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import M10Robot.orbs.BombOrb;
import M10Robot.orbs.PresentOrb;
import M10Robot.orbs.SearchlightOrb;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class Juggling extends AbstractDynamicCard implements BranchingUpgradesCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Juggling.class.getSimpleName());
    public static final String IMG = makeCardPath("Juggling.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int CARDS = 2;
    private static final int UPGRADE_PLUS_CARDS = 1;
    private static final int NTD = 1;

    // /STAT DECLARATION/


    public Juggling() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = NTD;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, secondMagicNumber)));
    }

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
        upgradeMagicNumber(UPGRADE_PLUS_CARDS);
    }

    public void branchUpgrade() {
        upgradeSecondMagicNumber(UPGRADE_PLUS_CARDS);
    }
}