package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractReloadableCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RecoilPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static M10Robot.M10RobotMod.makeCardPath;

public class FullAuto extends AbstractReloadableCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(FullAuto.class.getSimpleName());
    public static final String IMG = makeCardPath("FullAuto.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int HITS = 5;
    private static final int UPGRADE_PLUS_HITS = 1;
    private static final int SHOTS = 2;
    private static final int RECOIL = 1;

    private int lastChecked = HITS;

    // /STAT DECLARATION/

    public FullAuto() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
        secondMagicNumber = baseSecondMagicNumber = RECOIL;
        ammoCount = baseAmmoCount = thirdMagicNumber = baseThirdMagicNumber = SHOTS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < magicNumber ; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        }
        this.addToBot(new ApplyPowerAction(p, p, new RecoilPower(p, secondMagicNumber)));
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

    @Override
    public void changeDescription() {
        if (lastChecked != magicNumber && cardStrings != null) {
            lastChecked = magicNumber;
            if (magicNumber > HITS) {
                this.name = EXTENDED_DESCRIPTION[0];
            } else {
                this.name = NAME;
            }
            for (int i = 0 ; i < timesUpgraded ; i++) {
                upgradeName();
                --timesUpgraded;
            }
        }
    }
}
