package M10Robot.cutStuff;

import M10Robot.M10RobotMod;
import M10Robot.actions.IntensifyAction;
import M10Robot.cardModifiers.SpikyModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class WindUp extends AbstractDynamicCard implements BranchingUpgradesCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(WindUp.class.getSimpleName());
    public static final String IMG = makeCardPath("WindUp.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    private static final int DAMAGE = 6;
    private static final int SCALE = 3;
    private static final int UPGRADE_PLUS_SCALE = 3;

    // /STAT DECLARATION/

    public WindUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = SCALE;
        CardModifierManager.addModifier(this, new SpikyModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect e = damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), e));
        this.addToBot(new IntensifyAction(this, magicNumber, IntensifyAction.EffectType.DAMAGE));
    }

    //Upgraded stats.
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
        upgradeMagicNumber(UPGRADE_PLUS_SCALE);
    }

    public void branchUpgrade() {
        upgradeBaseCost(UPGRADE_COST);
    }
}
