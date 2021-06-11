package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.cards.modules.ScrapArmor;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class ArmorModule2 extends CustomRelic {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("ArmorModule2");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArmorModule2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArmorModule2.png"));

    public static final int AMOUNT = 6;

    public ArmorModule2() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override //Should replace default relic. Thanks kiooeht#3584 10/25/2020, #modding-technical
    public void obtain() {
        //Grab the player
        AbstractPlayer p = AbstractDungeon.player;
        //If we have the starter relic...
        if (p.hasRelic(ArmorModule.ID)) {
            //Find it...
            for (int i = 0; i < p.relics.size(); ++i) {
                if (p.relics.get(i).relicId.equals(ArmorModule.ID)) {
                    //Replace it
                    instantObtain(p, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    //Only spawn if we have Boo Sheet
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ArmorModule.ID);
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
        card.upgrade();
        this.addToBot(new MakeTempCardInHandAction(card));
    }

}
