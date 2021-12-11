package M10Robot.cutStuff.relics;

import M10Robot.M10RobotMod;
import M10Robot.cutStuff.powers.ModulesPower;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class ModularBody extends CustomRelic /*implements CustomSavable<Integer>, ClickableRelic*/ {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("ModularBody");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmorModule.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArmorModule.png"));

    public static final int AMOUNT = 4;

    public ModularBody() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ModulesPower(AbstractDungeon.player, AMOUNT)));
    }
}
