package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.MultichannelAction;
import M10Robot.cardModifiers.AimedModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.abstractCards.AbstractReloadableCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static M10Robot.M10RobotMod.makeCardPath;

public class Byte extends AbstractDynamicCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Byte.class.getSimpleName());
    public static final String IMG = makeCardPath("Byte.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int ORBS = 2;

    // /STAT DECLARATION/

    public Byte() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = ORBS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
//        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
        this.addToBot(new MultichannelAction(new BitOrb(), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
