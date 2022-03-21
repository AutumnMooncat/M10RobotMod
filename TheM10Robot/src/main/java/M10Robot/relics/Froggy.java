package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.powers.SpikesPower;
import M10Robot.relics.interfaces.OnExtractRelic;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class Froggy extends CustomRelic implements OnExtractRelic {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("Froggy");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Froggy.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Froggy.png"));
    private static final Texture IMG2 = TextureLoader.getTexture(makeRelicPath("Froggy2.png"));
    private static final Texture OUTLINE2 = TextureLoader.getTexture(makeRelicOutlinePath("Froggy2.png"));

    public static final int AMOUNT = 1;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String EXTRACT_STAT = DESCRIPTIONS[2];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[3];
    private final String PER_TURN_STRING = DESCRIPTIONS[4];

    public Froggy() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
    }

    public String getStatsDescription() {
        return EXTRACT_STAT + stats.get(EXTRACT_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(EXTRACT_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(EXTRACT_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(EXTRACT_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(EXTRACT_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        Froggy newRelic = new Froggy();
        newRelic.stats = this.stats;
        return newRelic;
    }

    @Override
    public int modifyExtractAmount(int amount) {
        img = IMG2;
        outlineImg = OUTLINE2;
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                img = IMG;
                outlineImg = OUTLINE;
                this.isDone = true;
            }
        });
        stats.put(EXTRACT_STAT, stats.get(EXTRACT_STAT) + AMOUNT);
        return amount + AMOUNT;
    }

    @Override
    public void onExtractCard(AbstractCard c) {}
}
