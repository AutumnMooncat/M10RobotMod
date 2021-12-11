package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.SearchlightOrb;
import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class LightShow extends AbstractDynamicCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(LightShow.class.getSimpleName());
    public static final String IMG = makeCardPath("LightShow.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = -1;
    private static final int ORBS = 0;
    private static final int UPGRADE_PLUS_ORBS = 1;

    // /STAT DECLARATION/


    public LightShow() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ORBS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += ChemicalX.BOOST;
            p.getRelic("Chemical X").flash();
        }

        ArrayList<List<?>> lists = new ArrayList<>();
        lists.add(AbstractDungeon.player.hand.group);
        lists.add(AbstractDungeon.player.drawPile.group);
        lists.add(AbstractDungeon.player.discardPile.group);
        lists.add(AbstractDungeon.player.powers);
        lists.add(AbstractDungeon.player.relics);
        lists.add(CardModifierPatches.CardModifierFields.cardModifiers.get(this));
        for (List<?> list : lists) {
            for (Object item : list) {
                if (item instanceof XCostModifier) {
                    XCostModifier mod = (XCostModifier)item;
                    if (mod.xCostModifierActive(this)) {
                        effect += mod.modifyX(this);
                    }
                }
            }
        }

        effect += magicNumber;

        for (int i = 0 ; i < effect ; i++) {
            this.addToBot(new ChannelAction(new SearchlightOrb()));
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

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

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 0) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        }
    }
}
