package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.actions.EvokelessEvocationAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AutoCasterPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("AutoCasterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final Color color = new Color(0.0F, 1.0F, 0.0F, 1.0F);

    private int evokedThisTurn;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public AutoCasterPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.priority = Short.MAX_VALUE;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("attackBurn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        evokedThisTurn = 0;
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        if (evokedThisTurn < amount) {
            evokedThisTurn++;
            this.addToBot(new EvokelessEvocationAction(orb));
            flash();
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        this.color.a = c.a;
        c = this.color;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount-evokedThisTurn), x, y, this.fontScale, c);
    }

    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AutoCasterPower(owner, amount);
    }


}
