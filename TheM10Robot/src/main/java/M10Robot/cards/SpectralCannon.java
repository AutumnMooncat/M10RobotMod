package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractReloadableCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class SpectralCannon extends AbstractReloadableCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SpectralCannon.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 13;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int SHOTS = 1;

    // /STAT DECLARATION/

    public SpectralCannon() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        ammoCount = baseAmmoCount = thirdMagicNumber = baseThirdMagicNumber = SHOTS;
        isMultiDamage = true;
        isEthereal = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new VFXAction(new ExplosionSmallEffect(aM.hb.cX, aM.hb.cY), 0.1F));
                this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
            }
        }
        for (AbstractPower pow : p.powers) {
            pow.onDamageAllEnemies(multiDamage);
        }
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
