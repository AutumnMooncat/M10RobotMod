package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.patches.ForcedUpgradeField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashMap;

import static M10Robot.M10RobotMod.makeCardPath;

public class Ascension extends AbstractDynamicCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Ascension.class.getSimpleName());
    public static final String IMG = makeCardPath("Ascension.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public Ascension() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                //Map to store if cards were originally upgraded or not
                HashMap<AbstractCard, Boolean> upgradedMap = new HashMap<>();
                //Map to store if the card was allowed to upgrade or not
                HashMap<AbstractCard, Boolean> canUpgradeMap = new HashMap<>();
                //Loop through all cards first
                for (AbstractCard c : p.hand.group) {
                    //Grab the old upgraded variable
                    upgradedMap.put(c, c.upgraded);
                    //Grab if the card was allowed to upgrade
                    canUpgradeMap.put(c, c.canUpgrade());
                    //Set the card as not upgraded, since that's the check Armaments Action makes
                    c.upgraded = false;
                }
                //Add this to top, since we add the Armaments Action to top, this comes second
                this.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        //Loop through all cards again
                        for (AbstractCard c : p.hand.group) {
                            //If the card is in our map (it's possible new cards were added)
                            if (upgradedMap.containsKey(c)) {
                                //If the card isnt currently upgraded, that means we didnt choose it (since all cards were set as not upgraded)
                                if (!c.upgraded) {
                                    //Restore its old upgraded boolean
                                    c.upgraded = upgradedMap.get(c);
                                } else { //This is the card that we just upgraded, as its the only one possible to have the upgrade boolean true
                                    //Check to see if it was originally allowed to upgrade or not (some cards can upgrade more than one time)
                                    if (!canUpgradeMap.get(c)) {
                                        //If it wasn't allowed to upgrade before, we forced an upgrade. Save this is a field for use in making copies
                                        ForcedUpgradeField.wasForced.set(c, true);
                                    }
                                }
                            }
                        }
                        this.isDone = true;
                    }
                });
                this.addToTop(new ArmamentsAction(false));
                this.isDone = true;
            }
        });
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 1) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        }
    }
}
