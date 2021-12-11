package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.HeavyModifier;
import M10Robot.cardModifiers.SpikyModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.powers.RecoilPower;
import M10Robot.powers.SpikesPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import javax.smartcardio.Card;

import static M10Robot.M10RobotMod.makeCardPath;

public class SpikeBall extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SpikeBall.class.getSimpleName());
    public static final String IMG = makeCardPath("SpikeBall.png");
    // Setting the image as as easy as can possibly be now. You just need to provide the image name

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_PLUS_DMG = 6;
    private static final int SPIKES = 4;

    // /STAT DECLARATION/

    public SpikeBall() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = SPIKES;
        isMultiDamage = true;
        CardModifierManager.addModifier(this, new HeavyModifier());
        CardModifierManager.addModifier(this, new SpikyModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(m.hb.cX, m.hb.cY));
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                this.isDone = true;
            }
        });
        this.addToBot(new SFXAction("WATCHER_HEART_PUNCH"));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new ApplyPowerAction(p, p, new SpikesPower(p, magicNumber)));
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
