package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.relics.ProtectiveShell;
import M10Robot.relics.ProtectiveShell2;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ProtectPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("ProtectPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int HP_REDUCTION = 2;
    public static final int SCRAMBLE_AMOUNT = 1;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ProtectPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.BUFF;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("channel");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            if (AbstractDungeon.player.hasRelic(ProtectiveShell.ID)) {
                ((ProtectiveShell)AbstractDungeon.player.getRelic(ProtectiveShell.ID)).onProtectActivation(Math.min(HP_REDUCTION, damageAmount));
            } else if (AbstractDungeon.player.hasRelic(ProtectiveShell2.ID)) {
                ((ProtectiveShell2)AbstractDungeon.player.getRelic(ProtectiveShell2.ID)).onProtectActivation(Math.min(HP_REDUCTION, damageAmount));
            }
            damageAmount = Math.max(0, damageAmount-HP_REDUCTION);
            this.addToTop(new ReducePowerAction(owner, owner, this, 1));
            flash();
        }
        return damageAmount;
    }

    @Override
    public void onRemove() {
        if (AbstractDungeon.player.hasRelic(ProtectiveShell.ID)) {
            ((ProtectiveShell)AbstractDungeon.player.getRelic(ProtectiveShell.ID)).onScrambled(SCRAMBLE_AMOUNT);
        } else if (AbstractDungeon.player.hasRelic(ProtectiveShell2.ID)) {
            ((ProtectiveShell2)AbstractDungeon.player.getRelic(ProtectiveShell2.ID)).onScrambled(SCRAMBLE_AMOUNT);
        }
        this.addToTop(new ApplyPowerAction(owner, owner, new ScrambledPower(owner, SCRAMBLE_AMOUNT)));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + HP_REDUCTION + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ProtectPower(owner, amount);
    }

}