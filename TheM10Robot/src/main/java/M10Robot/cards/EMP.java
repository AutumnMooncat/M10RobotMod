package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractReloadableCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.EMPPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class EMP extends AbstractReloadableCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(EMP.class.getSimpleName());
    public static final String IMG = makeCardPath("EMP.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final AbstractCard.CardType TYPE = CardType.ATTACK;
    public static final AbstractCard.CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int STASIS = 1;
    private static final int UPGRADE_PLUS_STASIS = 1;

    // /STAT DECLARATION/

    public EMP() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = STASIS;
        this.isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Electro Sound
        this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
        this.addToBot(new SFXAction("ORB_LIGHTNING_EVOKE"));
        //Pulse Wave
        this.addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.BLUE.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3f));
        //loop monsters
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                //play Shock sfx on hit
                this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
                //Deal damage
                this.addToBot(new DamageAction(aM, new DamageInfo(p, this.multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageTypeForTurn), true));
                //check if unblocked to apply stasis
                //this.addToBot(new CheckIfUnblockedAction(aM, p, new ApplyPowerAction(aM, p, new EMPPower(aM, magicNumber), magicNumber, true)));
                this.addToBot(new ApplyPowerAction(aM, p, new EMPPower(aM, magicNumber), magicNumber, true));

            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_STASIS);
            initializeDescription();
        }
    }
}
