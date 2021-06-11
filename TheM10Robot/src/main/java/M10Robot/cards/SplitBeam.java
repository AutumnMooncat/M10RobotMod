package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
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
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.HashMap;

import static M10Robot.M10RobotMod.makeCardPath;

public class SplitBeam extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SplitBeam.class.getSimpleName());
    public static final String IMG = makeCardPath("PlaceholderAttack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int HITS = 2;
    private static final int UPGRADE_PLUS_HITS = 1;

    // /STAT DECLARATION/

    public SplitBeam() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = HITS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        HashMap<AbstractMonster, Integer> hitMap = new HashMap<>();
        for (int i = 0; i < magicNumber ; i++) {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            hitMap.put(mo, hitMap.getOrDefault(mo, 0)+1);
            this.addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            this.addToBot(new VFXAction(new SmallLaserEffect(mo.hb.cX, mo.hb.cY, p.hb.cX, p.hb.cY), 0.1F));
            this.addToBot(new DamageAction(mo, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
            this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, secondMagicNumber, false), secondMagicNumber, true));
        }

        for (AbstractMonster mo : hitMap.keySet()) {
            if (hitMap.get(mo) > 1) {
                this.addToBot(new VFXAction(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY), 0.1F));
                this.addToBot(new DamageAction(mo, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
                this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, secondMagicNumber, false), secondMagicNumber, true));
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
        }
    }
}
