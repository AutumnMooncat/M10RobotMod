package M10Robot.cutCards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import basemod.BaseMod;
import basemod.interfaces.PostEnergyRechargeSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class FeedForward extends AbstractDynamicCard implements PostEnergyRechargeSubscriber {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(FeedForward.class.getSimpleName());
    public static final String IMG = makeCardPath("FeedForward.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DAMAGE_INCREASE = 2;
    private static final int UPGRADE_PLUS_DAMAGE_INCREASE = 1;

    private boolean playedThisTurn = false;

    // /STAT DECLARATION/

    //TODO upgrade ALL Feed Forward cards?
    public FeedForward() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_INCREASE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        this.addToBot(new VFXAction(new SmallLaserEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY), 0.0F));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
        BaseMod.subscribe(this);
        playedThisTurn = true;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c != this && playedThisTurn) {
            //CardCrawlGame.sound.play("CARD_UPGRADE", 0.1F);
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_INCREASE);
            initializeDescription();
        }
    }

    @Override
    public void receivePostEnergyRecharge() {
        playedThisTurn = false;
        BaseMod.unsubscribeLater(this);
    }
}
