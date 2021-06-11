package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.cards.modules.ScrapArmor;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class ArmorModule extends CustomRelic /*implements CustomSavable<Integer>, ClickableRelic*/ {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("ArmorModule");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmorModule.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArmorModule.png"));

    public static final int AMOUNT = 4;

    public ArmorModule() {
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
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractCard card = new ScrapArmor();
        this.addToBot(new MakeTempCardInHandAction(card));
    }
}
