package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.SelfDestructAction2;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RecoilPower;
import M10Robot.vfx.BurnToAshEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class SelfDestruction extends AbstractDynamicCard implements CannotOverclock {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SelfDestruction.class.getSimpleName());
    public static final String IMG = makeCardPath("SelfDestruction2.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 3;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DAMAGE = 8;
    private static final int SELF_DAMAGE = 6;

    // /STAT DECLARATION/

    public SelfDestruction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = SELF_DAMAGE;
        exhaust = true;
        //CardModifierManager.addModifier(this, new HeavyModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                AbstractDungeon.effectsQueue.add(new BurnToAshEffect(m.hb.cX, m.hb.cY));
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.15f);
                CardCrawlGame.sound.play("GHOST_ORB_IGNITE_2", 0.15f);
                CardCrawlGame.sound.play("WATCHER_HEART_PUNCH", 0.15f);
                AbstractDungeon.effectsQueue.add(new ShockWaveEffect(m.hb.cX, m.hb.cY, Color.RED.cpy(), ShockWaveEffect.ShockWaveType.CHAOTIC));
                this.isDone = true;
            }
        });
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));*/
        this.addToBot(new SelfDestructAction2(p, m, new DamageInfo(p, damage, damageTypeForTurn), secondMagicNumber));
        //We really don't want this to interact with Stasis
        StunMonsterPower pow = new StunMonsterPower(m, 1);
        pow.type = NeutralPowertypePatch.NEUTRAL;
        this.addToBot(new ApplyPowerAction(m, p, pow, 1));
        //this.addToBot(new ApplyPowerAction(p, p, new RecoilPower(p, 1)));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
