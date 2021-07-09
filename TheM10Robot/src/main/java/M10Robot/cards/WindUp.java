package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class WindUp extends AbstractDynamicCard implements CustomSavable<Integer>, BranchingUpgradesCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(WindUp.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 1;
    private static final int SCALE = 1;
    private static final int UPGRADE_PLUS_SCALE = 1;

    // /STAT DECLARATION/

    public WindUp() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        misc = DAMAGE;
        baseDamage = damage = misc;
        magicNumber = baseMagicNumber = SCALE;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction.AttackEffect e = damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), e));
        for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid)) {
                c.misc += this.magicNumber;
                c.baseDamage = c.misc;
                c.damage = c.misc;
                c.initializeDescription();

            }
        }
        this.misc += this.magicNumber;
        this.baseDamage = this.misc;
        this.damage = this.misc;
        applyPowers();
    }

    public void applyPowers() {
        this.baseDamage = this.misc;
        super.applyPowers();
        this.initializeDescription();
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
        this.isInnate = true;
        rawDescription = UPGRADE_DESCRIPTION;
    }

    @Override
    public Integer onSave() {
        return this.misc;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer != null) {
            this.misc = integer;
            this.baseDamage = this.misc;
            this.damage = this.misc;
            initializeDescription();
        }
    }
}
