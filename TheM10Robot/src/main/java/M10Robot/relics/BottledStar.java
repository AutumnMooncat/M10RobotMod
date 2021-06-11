package M10Robot.relics;

import M10Robot.M10RobotMod;
import M10Robot.patches.relics.BottleFields;
import M10Robot.util.TextureLoader;
import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import static M10Robot.M10RobotMod.makeRelicOutlinePath;
import static M10Robot.M10RobotMod.makeRelicPath;

public class BottledStar extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {

    // ID, images, text.
    public static final String ID = M10RobotMod.makeID("BottledStar");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BottledStar.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("EmptyBG.png"));

    private static final int DAMAGE = 5;

    private boolean cardSelected = true;
    public AbstractCard card;

    HashMap<String, Integer> stats = new HashMap<>();
    private final String DAMAGE_STAT = DESCRIPTIONS[4];
    private final String PER_COMBAT_STRING = DESCRIPTIONS[5];
    private final String PER_TURN_STRING = DESCRIPTIONS[6];

    public BottledStar() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
        resetStats();
    }

    @Override
    public void onEquip() { // 1. When we acquire the relic
        cardSelected = false; // 2. Tell the relic that we haven't bottled the card yet
        if (AbstractDungeon.isScreenUp) { // 3. If the map is open - hide it.
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        // 4. Set the room to INCOMPLETE - don't allow us to use the map, etc.
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck); // 5. Get a card group of all currently unbottled cards
        AbstractDungeon.gridSelectScreen.open(group, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, false, false, false);
        // 6. Open the grid selection screen with the cards from the CardGroup we specified above. The description reads "Select a card to bottle for" + (relic name) + "."
    }

    @Override
    public void onUnequip() { // 1. On unequip
        if (card != null) { // If the bottled card exists (prevents the game from crashing if we removed the bottled card from our deck for example.)
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card); // 2. Get the card
            if (cardInDeck != null) {
                BottleFields.inBottledStar.set(cardInDeck, false); // In our SpireField - set the card to no longer be bottled. (Unbottle it)
            }
        }
    }

    @Override
    public void update() {
        super.update(); //Do all of the original update() method in AbstractRelic

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // If the card hasn't been bottled yet and we have cards selected in the gridSelectScreen (from onEquip)
            cardSelected = true; //Set the cardSelected boolean to be true - we're about to bottle the card.
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0); // The custom Savable "card" is going to equal
            // The card from the selection screen (it's only 1, so it's at index 0)
            BottleFields.inBottledStar.set(card, true); // Use our custom spire field to set that card to be bottled.
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; // The room phase can now be set to complete (From INCOMPLETE in onEquip)
            AbstractDungeon.gridSelectScreen.selectedCards.clear(); // Always clear your grid screen after using it.
            setDescriptionAfterLoading(); // Set the description to reflect the bottled card (the method is at the bottom of this file)
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public boolean canSpawn() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.rarity != AbstractCard.CardRarity.BASIC) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return BottleFields.inBottledStar::get;
    }


    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (BottleFields.inBottledStar.get(c)) {
            this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SMASH));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (!mo.isDeadOrEscaped()) {
                    stats.put(DAMAGE_STAT, stats.get(DAMAGE_STAT) + DAMAGE);
                }
            }
        }
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex)
    {
        // onLoad automatically has the Integer saved in onSave upon loading into the game.

        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                BottleFields.inBottledStar.set(card, true);
                setDescriptionAfterLoading();
            }
        }
        // Uses the card's index saved before to search for the card in the deck and put it in a custom SpireField.
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        BottledStar newRelic = new BottledStar();
        newRelic.stats = this.stats;
        return newRelic;
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
}
