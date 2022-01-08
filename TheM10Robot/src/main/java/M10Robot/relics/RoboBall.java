package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.characters.M10Robot;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class RoboBall extends CustomRelic {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("RoboBall");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("RoboBall.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("RoboBall.png"));

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[1];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[2];
    private final String PER_TURN_STRING = DESCRIPTIONS[3];

    public RoboBall() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
        resetStats();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        grayscale = false;
        beginLongPulse();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (!grayscale) {
            if (info.output >= 0 && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != AbstractDungeon.player) {
                this.flash();
                stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + info.output);
                this.addToTop(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, info.output, DamageInfo.DamageType.THORNS), info.output > 10 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                if (AbstractDungeon.player instanceof M10Robot) {
                    this.addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            ((M10Robot) AbstractDungeon.player).playAnimation("attack");
                            this.isDone = true;
                        }
                    });
                }
                grayscale = true;
                stopPulse();
            }
        }
        return 0;
    }

    public String getStatsDescription() {
        return DAMAGE_STAT + stats.get(DAMAGE_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(DAMAGE_STAT);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        builder.append(PER_TURN_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT_STRING);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats.put(DAMAGE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DAMAGE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DAMAGE_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        RoboBall newRelic = new RoboBall();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
