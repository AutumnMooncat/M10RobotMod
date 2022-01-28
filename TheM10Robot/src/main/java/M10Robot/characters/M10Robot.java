package M10Robot.characters;

import M10Robot.CustomAnimationListener;
import M10Robot.CustomSpriterAnimation;
import M10Robot.M10RobotMod;
import M10Robot.RandomChatterHelper;
import M10Robot.cards.Defend;
import M10Robot.cards.MagnumStrike;
import M10Robot.cards.Strike;
import M10Robot.cards.Surveillance;
import M10Robot.cards.interfaces.SkillAnimationAttack;
import M10Robot.relics.DualCore;
import M10Robot.vfx.StarBreakerVictoryEffect;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.*;
import static M10Robot.characters.M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in DefaultMod-character-Strings.json in the resources

public class M10Robot extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(M10RobotMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_MIO_ROBOT;
        @SpireEnum(name = "GREEN_SPRING_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor GREEN_SPRING_CARD_COLOR; //Jet Stream, Opal, White Ice, Botticelli~, Gulf Stream~,
        @SpireEnum(name = "GREEN_SPRING_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 3;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("M10Robot");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    //private static final String defaultAnim = "M10RobotResources/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml";
    private static final String ANIM_FILE = "M10RobotResources/images/char/tachy/Spriter/Tachy.scml";

    //private static final SpriterAnimation defaultAnimation = new SpriterAnimation(defaultAnim);

    //Use a new Animation rather than the same one so we dont spawn in the dead animation if we lose a run and start a new one, lml
    //private static final CustomSpriterAnimation ANIM_SET = new CustomSpriterAnimation(ANIM_FILE);+


    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "M10RobotResources/images/char/tachy/orb/layer1.png",
            "M10RobotResources/images/char/tachy/orb/layer2.png",
            "M10RobotResources/images/char/tachy/orb/layer3.png",
            "M10RobotResources/images/char/tachy/orb/layer4.png",
            "M10RobotResources/images/char/tachy/orb/layer5.png",
            "M10RobotResources/images/char/tachy/orb/layer6.png",
            "M10RobotResources/images/char/tachy/orb/layer1d.png",
            "M10RobotResources/images/char/tachy/orb/layer2d.png",
            "M10RobotResources/images/char/tachy/orb/layer3d.png",
            "M10RobotResources/images/char/tachy/orb/layer4d.png",
            "M10RobotResources/images/char/tachy/orb/layer5d.png",};

    public static final float[] layerSpeeds = {-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
    //public static final float[] layerSpeeds = {-20.0F, 20.0F, -40.0F, 40.0F, 0.0F};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public M10Robot(String name, PlayerClass setClass) {
        //super(name, setClass, orbTextures, "M10RobotResources/images/char/defaultCharacter/orb/vfxm.png", null, idleAnimation);
        super(name, setClass, new DoubleEnergyOrb(orbTextures, "M10RobotResources/images/char/tachy/orb/vfx.png", layerSpeeds), new CustomSpriterAnimation(ANIM_FILE));
        DoubleEnergyOrb.DoubleOrbField.isDoubleOrb.set(this, true);
        Player.PlayerListener listener = new CustomAnimationListener(this);
        ((CustomSpriterAnimation)this.animation).myPlayer.addListener(listener);


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                M10_SHOULDER_2, // campfire pose
                M10_SHOULDER_1, // another campfire pose
                M10_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        /*
        loadAnimation(THE_DEFAULT_SKELETON_ATLAS, THE_DEFAULT_SKELETON_JSON, 1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        */

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        //logger.info("GetLoadout, crash?");
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        //* Main Deck

        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(MagnumStrike.ID);
        //retVal.add(PolishingKit.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Surveillance.ID);

        //*/

        //logger.info("Starter Deck, crash?");
        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(DualCore.ID);
        //retVal.add(ProtectiveShell.ID);
        //retVal.add(ModularBody.ID);

        UnlockTracker.markRelicAsSeen(DualCore.ID);
        //UnlockTracker.markRelicAsSeen(ProtectiveShell.ID);
        //UnlockTracker.markRelicAsSeen(ModularBody.ID);

        //logger.info("Starter Relic, crash?");
        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return GREEN_SPRING_CARD_COLOR;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return GREEN_SPRING.cpy();
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Surveillance();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new M10Robot(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return M10RobotMod.GREEN_SPRING.cpy();
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return M10RobotMod.GREEN_SPRING.cpy();
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.FIRE};
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("M10RobotResources/images/scene/greyBg.jpg");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("M10RobotResources/images/scene/starbreaker1.png", "ATTACK_MAGIC_FAST_3"));
        panels.add(new CutscenePanel("M10RobotResources/images/scene/starbreaker2.png", "TURN_EFFECT"));
        panels.add(new CutscenePanel("M10RobotResources/images/scene/starbreaker3.png", "CEILING_BOOM_3"));
        return panels;
    }

    @Override
    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        effects.add(new StarBreakerVictoryEffect());
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public void onVictory() {
        super.onVictory();
        playAnimation("happy");
    }

    public void refreshHealthBar() {
        ReflectionHacks.setPrivate(this, AbstractCreature.class, "healthBarWidth", this.hb.width * (float)this.currentHealth / (float)this.maxHealth);
        ReflectionHacks.setPrivate(this, AbstractCreature.class, "targetHealthBarWidth", this.hb.width * (float)this.currentHealth / (float)this.maxHealth);
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
        switch (c.type) {
            case ATTACK:
                RandomChatterHelper.showChatter(RandomChatterHelper.getAttackText(), cardTalkProbability, enableCardBattleTalkEffect);
                if (c instanceof SkillAnimationAttack) {
                    playAnimation("skill");
                } else {
                    playAnimation("attack");
                }
                break;
            case POWER:
                RandomChatterHelper.showChatter(RandomChatterHelper.getPowerText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("happy");
                break;
            default:
                RandomChatterHelper.showChatter(RandomChatterHelper.getSkillText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("skill");
                break;
        }
    }

    public void damage(DamageInfo info) {
        boolean hadBlockBeforeSuper = this.currentBlock > 0;
        super.damage(info);
        boolean hasBlockAfterSuper = this.currentBlock > 0;
        boolean tookNoDamage = this.lastDamageTaken == 0;
        if (hadBlockBeforeSuper && (hasBlockAfterSuper || tookNoDamage)) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBlockedDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            playAnimation("happy");
        } else {
            if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
                if (info.output >= 15) {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getHeavyDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                } else {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getLightDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                }
            } else if (info.type == DamageInfo.DamageType.THORNS && info.output > 0) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getFieldDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            }
            playAnimation("hurt");
        }
    }

    public CustomSpriterAnimation getAnimation() {
        return (CustomSpriterAnimation) this.animation;
    }

    public void playAnimation(String name) {
        ((CustomSpriterAnimation)this.animation).myPlayer.setAnimation(name);
    }

    public void stopAnimation() {
        CustomSpriterAnimation anim = (CustomSpriterAnimation) this.animation;
        int time = anim.myPlayer.getAnimation().length;
        anim.myPlayer.setTime(time);
        anim.myPlayer.speed = 0;
    }

    public void resetToIdleAnimation() {
        playAnimation("idle");
    }

    @Override
    public void playDeathAnimation() {
        RandomChatterHelper.showChatter(RandomChatterHelper.getKOText(), preTalkProbability, enablePreBattleTalkEffect); // I don't think this works
        playAnimation("ko");
    }

    @Override
    public void heal(int healAmount) {
        if (healAmount > 0) {
            if (RandomChatterHelper.showChatter(RandomChatterHelper.getHealingText(), damagedTalkProbability, enableDamagedBattleTalkEffect)){ //Technically changes your hp, lol
                playAnimation("happy");
            }
        }
        super.heal(healAmount);
    }

    @Override
    public void preBattlePrep() {
        playAnimation("idle");
        super.preBattlePrep();
        boolean bossFight = false;
        for (AbstractMonster mons : AbstractDungeon.getMonsters().monsters) {
            if (mons.type == AbstractMonster.EnemyType.BOSS) {
                bossFight = true;
                break;
            }
        }
        if (AbstractDungeon.getCurrRoom().eliteTrigger || bossFight) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBossFightText(), preTalkProbability, enablePreBattleTalkEffect);
        } else {
            if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth*0.5f) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getLowHPBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            } else {
                RandomChatterHelper.showChatter(RandomChatterHelper.getBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            }
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}
