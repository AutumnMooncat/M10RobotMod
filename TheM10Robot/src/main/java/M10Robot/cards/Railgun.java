package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.SkillAnimationAttack;
import M10Robot.characters.M10Robot;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class Railgun extends AbstractDynamicCard implements SkillAnimationAttack {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Railgun.class.getSimpleName());
    public static final String IMG = makeCardPath("Railgun.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/

    public Railgun() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY), 0.4F));
        this.addToBot(new SFXAction("ORB_PLASMA_CHANNEL"));
        //this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F)); //pew oew laser
        //this.addToBot(new VFXAction(new SmallLaserEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY), 0.1F)); //aimed laser shot
        //this.addToBot(new VFXAction(new InversionBeamEffect(m.hb.cX))); //Good but for something else, like airstrike
        /*Vector2 vec = new Vector2(m.hb.cX - p.hb.cX, m.hb.cY - p.hb.cY);
        vec.nor();
        float speed = 10f * Settings.scale;
        this.addToBot(new VFXAction(new FlyingSpikeEffect(p.hb.cX, p.hb.cY, vec.angle(), vec.x*speed, vec.y*speed, Color.GREEN)));*/ //I cant tell what this is doing
        //this.addToBot(new VFXAction(new FlyingOrbEffect(m.hb.cX, m.hb.cY))); //Bad, you are always the target
        //this.addToBot(new VFXAction(new SwirlyBloodEffect(m.hb.cX, m.hb.cY))); // Spins outwards
        //this.addToBot(new VFXAction(new VerticalAuraEffect(Color.ORANGE, m.hb.cX, m.hb.cY))); //poof-y clouds appear
        //this.addToBot(new VFXAction(new WobblyLineEffect(m.hb.cX, m.hb.cY, Color.ORANGE))); //Particles explode out of target, looks nice
        //this.addToBot(new VFXAction(new BossCrystalImpactEffect(m.hb.cX, m.hb.cY))); //Weird pulse with heartbeat sound
        //this.addToBot(new VFXAction(new BlizzardEffect(10, false))); //hailstorm that drops ice shards
        //this.addToBot(new VFXAction(new FlickCoinEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY))); //throws coin in big arc
        //this.addToBot(new VFXAction(new IceShatterEffect(m.hb.cX, m.hb.cY))); //idk what this is doing. some particles appear around the target?
        //this.addToBot(new VFXAction(new ImpactSparkEffect(m.hb.cX, m.hb.cY))); //particles fly off the target?
        //this.addToBot(new VFXAction(new LaserBeamEffect(m.hb.cX, m.hb.cY))); //Bad. Mind blast but targets the player
        //this.addToBot(new VFXAction(new InflameEffect(m))); //flames pop up momentarily, looks nice
        //this.addToBot(new VFXAction(new WaterSplashParticleEffect(m.hb.cX, m.hb.cY))); //does nothing?
        //this.addToBot(new VFXAction(new WarningSignEffect(m.hb.cX, m.hb.cY))); //flashes a red ! triangle
        //this.addToBot(new VFXAction(new BlockedWordEffect(m, p.hb.cX, p.hb.cY, "stuff"))); //Displays the message above the x/y? seems to ignore target input

        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p instanceof M10Robot) {
                    ((M10Robot) p).playAnimation("attack");
                }
                this.isDone = true;
            }
        });
        AbstractGameEffect shootCoin = new VfxBuilder(ImageMaster.vfxAtlas.findRegion("combat/empowerCircle1"), p.hb.cX, p.hb.cY, 0.1f)
                .setScale(0.5f)
                .setColor(Color.GOLD)
                .emitEvery(ShineSparkleEffect::new, 0.005f)
                .moveX(p.hb.cX, m.hb.cX, VfxBuilder.Interpolations.LINEAR)
                .moveY(p.hb.cY, m.hb.cY, VfxBuilder.Interpolations.LINEAR)
                .rotate(-400f)
                .build();
        this.addToBot(new VFXAction(shootCoin));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (shootCoin.isDone) {
                    p.loseGold(1);
                    this.isDone = true;
                }
            }
        });
        this.addToBot(new SFXAction("WATCHER_HEART_PUNCH"));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (p.gold <= 0) {
            canUse = false;
            this.cantUseMessage = EXTENDED_DESCRIPTION[0];
        }
        return canUse;
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
