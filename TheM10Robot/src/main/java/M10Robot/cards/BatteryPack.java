package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractClickableCard;
import M10Robot.characters.M10Robot;
import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class BatteryPack extends AbstractClickableCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(BatteryPack.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderSkill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = -1;
    private static final int MAX_CHARGES = 3;
    private static final int UPGRADE_PLUS_MAX_CHARGES = 2;

    // /STAT DECLARATION/


    public BatteryPack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAX_CHARGES;
        ammoCount = baseAmmoCount = 0;
        selfRetain = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
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

        this.ammoCount = Math.min(ammoCount+effect, magicNumber);
        isAmmoCountModified = ammoCount != baseAmmoCount;

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAX_CHARGES);
            initializeDescription();
        }
    }

    @Override
    public void onRightClick() {
        if (ammoCount > 0) {
            ammoCount--;
            isAmmoCountModified = ammoCount != baseAmmoCount;
            this.addToBot(new GainEnergyAction(1));
            superFlash();
        }
    }
}
