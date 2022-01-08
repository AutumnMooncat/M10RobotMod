package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.powers.ProtectPower;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class ProtectiveShell2 extends CustomRelic {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("ProtectiveShell2");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("M102.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("M10.png"));

    public static final int STACKS = 5;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_MITIGATED = DESCRIPTIONS[2];
    private final String SCRAMBLES = DESCRIPTIONS[3];

    public ProtectiveShell2() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
        resetStats();
    }

    @Override //Should replace default relic. Big thanks papa kio
    public void obtain() {
        //Grab the player
        AbstractPlayer p = AbstractDungeon.player;
        //If we have the starter relic...
        if (p.hasRelic(ProtectiveShell.ID)) {
            //Grab its data for relic stats if you want to carry the stats over to the boss relic
            ProtectiveShell r = (ProtectiveShell) p.getRelic(ProtectiveShell.ID);
            stats.put(DAMAGE_MITIGATED, r.getMitigation());
            stats.put(SCRAMBLES, r.getScrambles());
            //Find it...
            for (int i = 0; i < p.relics.size(); ++i) {
                if (p.relics.get(i).relicId.equals(ProtectiveShell.ID)) {
                    //Replace it
                    instantObtain(p, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    //Only spawn if we have the starter relic
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ProtectiveShell.ID);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STACKS + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ProtectPower(AbstractDungeon.player, STACKS)));
    }

    public void onProtectActivation(int mitigatedAmount) {
        stats.put(DAMAGE_MITIGATED, stats.get(DAMAGE_MITIGATED)+mitigatedAmount);
    }

    public void onScrambled(int scrambledAmount) {
        stats.put(SCRAMBLES, stats.get(SCRAMBLES)+scrambledAmount);
    }

    public String getStatsDescription() {
        return DAMAGE_MITIGATED + stats.get(DAMAGE_MITIGATED) + SCRAMBLES + stats.get(SCRAMBLES);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(DAMAGE_MITIGATED, 0);
        stats.put(SCRAMBLES, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_MITIGATED));
        statsToSave.add(stats.get(SCRAMBLES));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_MITIGATED, jsonArray.get(0).getAsInt());
            stats.put(SCRAMBLES, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        ProtectiveShell2 newRelic = new ProtectiveShell2();
        newRelic.stats = this.stats;
        return newRelic;
    }

}
