package M10Robot.powers;

import M10Robot.M10RobotMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RecoveryModePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("RecoveryModePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int MAX_PER_APPLICATION = 5;
    public int maxTempHP;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public RecoveryModePower(AbstractCreature owner, int amount) {
        this(owner, amount, MAX_PER_APPLICATION);
    }

    public RecoveryModePower(AbstractCreature owner, int amount, int maxTempHP) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.maxTempHP = maxTempHP;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("deva");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.maxTempHP += MAX_PER_APPLICATION;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        int amt = Math.min(amount, Math.max(0, maxTempHP - TempHPField.tempHp.get(owner)));
        if (amt > 0) {
            flash();
            this.addToTop(new AddTemporaryHPAction(owner, owner, amt));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + maxTempHP + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RecoveryModePower(owner, amount, maxTempHP);
    }

}