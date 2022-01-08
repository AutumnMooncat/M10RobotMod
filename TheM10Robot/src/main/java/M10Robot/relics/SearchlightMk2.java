package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.orbs.SearchlightOrb;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class SearchlightMk2 extends CustomRelic {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("SearchlightMk2");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Searchlight.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Searchlight.png"));

    public static final int UPGRADES = 1;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String UPGRADES_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public SearchlightMk2() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + UPGRADES + DESCRIPTIONS[1];
    }

    @Override
    public void onTrigger() {
        flash();
        stats.put(UPGRADES_STAT, stats.get(UPGRADES_STAT) + UPGRADES);
    }

    @Override
    public void atBattleStart() {
        flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ChannelAction(new SearchlightOrb()));
    }

    public String getStatsDescription() {
        return UPGRADES_STAT + stats.get(UPGRADES_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(UPGRADES_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }
    public void resetStats() {
        stats.put(UPGRADES_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(UPGRADES_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(UPGRADES_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        SearchlightMk2 newRelic = new SearchlightMk2();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
