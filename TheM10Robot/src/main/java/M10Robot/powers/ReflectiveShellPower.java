package M10Robot.powers;

import M10Robot.M10RobotMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReflectiveShellPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("ReflectiveShellPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ReflectiveShellPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.priority = 0;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("buffer");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != owner && info.owner != null) {
            flash();
            this.addToBot(new DamageAction(info.owner, new DamageInfo(owner, damageAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
        }
        return 0;
    }

    @Override
    public void atStartOfTurn() {
        if (amount == 1) {
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            this.addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReflectiveShellPower(owner, amount);
    }

}