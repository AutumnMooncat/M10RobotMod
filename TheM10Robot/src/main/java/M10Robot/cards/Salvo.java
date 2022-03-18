package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.SalvoAction;
import M10Robot.cardModifiers.AimedModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.orbs.OrbUpgradeField;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import static M10Robot.M10RobotMod.makeCardPath;

public class Salvo extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Salvo.class.getSimpleName());
    public static final String IMG = makeCardPath("Salvo2.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BONUS = 1;
    private static final int UPGRADE_PLUS_BONUS = 1;

    // /STAT DECLARATION/

    public Salvo() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        baseMagicNumber = magicNumber = BONUS;
        //CardModifierManager.addModifier(this, new AimedModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SalvoAction(p, m, new DamageInfo(p, damage, damageTypeForTurn), false));
    }

    @Override
    public void applyPowers() {
        int base = baseDamage;
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (OrbUpgradeField.UpgradeCount.timesUpgraded.get(orb) > 0 || (orb instanceof AbstractCustomOrb && ((AbstractCustomOrb) orb).timesUpgraded > 0)) {
                baseDamage += magicNumber;
            }
        }
        super.applyPowers();
        baseDamage = base;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int base = baseDamage;
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (OrbUpgradeField.UpgradeCount.timesUpgraded.get(orb) > 0 || (orb instanceof AbstractCustomOrb && ((AbstractCustomOrb) orb).timesUpgraded > 0)) {
                baseDamage += magicNumber;
            }
        }
        super.calculateCardDamage(mo);
        baseDamage = base;
        isDamageModified = damage != baseDamage;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_BONUS);
            initializeDescription();
        }
    }
}
