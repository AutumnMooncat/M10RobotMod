package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.SearchlightOrb;
import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class LightShow extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(LightShow.class.getSimpleName());
    public static final String IMG = makeCardPath("LightShow.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int ORBS = 1;

    // /STAT DECLARATION/


    public LightShow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ORBS;
        exhaust = true;
        showEvokeValue = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int skills = (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(c -> c.type == CardType.SKILL).count();
        this.addToBot(new MultichannelAction(new SearchlightOrb(), magicNumber*skills));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.info = this.baseInfo = (int) AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().filter(c -> c.type == CardType.SKILL).count();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            initializeDescription();
        }
    }
}
