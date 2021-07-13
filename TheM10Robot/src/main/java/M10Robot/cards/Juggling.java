package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import M10Robot.orbs.BombOrb;
import M10Robot.orbs.PresentOrb;
import M10Robot.orbs.SearchlightOrb;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.FocusPower;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class Juggling extends AbstractDynamicCard {

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
    private static final int ORBS = 2;
    private static final int UPGRADE_PLUS_ORBS = 1;
    private static final int FOCUS_LOST = 1;

    // /STAT DECLARATION/


    public Juggling() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ORBS;
        secondMagicNumber = baseSecondMagicNumber = FOCUS_LOST;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < magicNumber ; i++) {
            ArrayList<AbstractOrb> orbs = new ArrayList<>();
            orbs.add(new SearchlightOrb());
            orbs.add(new BitOrb());
            orbs.add(new BombOrb());
            orbs.add(new PresentOrb());
            this.addToBot(new ChannelAction(orbs.get(AbstractDungeon.cardRandomRng.random(orbs.size() - 1))));
        }
        this.addToBot(new ApplyPowerAction(p, p, new FocusPower(p, -secondMagicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ORBS);
            initializeDescription();
        }
    }
}