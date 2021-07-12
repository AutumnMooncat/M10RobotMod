package M10Robot;

import M10Robot.potions.*;
import M10Robot.relics.*;
import M10Robot.variables.*;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import M10Robot.characters.M10Robot;
import M10Robot.util.IDCheckDontTouchPls;
import M10Robot.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
//Done: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//Done: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class M10RobotMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(M10RobotMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theShadowSirenDefaultSettings = new Properties();
    public static Properties normaBackupVars = new Properties();

    public static final String ENABLE_SELFDAMAGE_SETTING = "enableSelfDamage";
    public static boolean enableSelfDamage = false; // The boolean we'll be setting on/off (true/false)

    public static final String FIVE_STAR_WANTED_SETTING = "enableStrongerWantedEffect";
    public static boolean enableStrongerWantedEffect = false;

    public static final String DISABLE_GULL_VFX = "disableGullVfx";
    public static boolean disableGullVfx = false;

    public static final String CARD_BATTLE_TALK_SETTING = "enableCardBattleTalk";
    public static boolean enableCardBattleTalkEffect = true;

    public static final String CARD_BATTLE_TALK_PROBABILITY_SETTING = "cardTalkProbability";
    private static final int BASE_CARD_TALK_PROBABILITY = 0;
    public static int cardTalkProbability = BASE_CARD_TALK_PROBABILITY; //Out of 100

    public static final String DAMAGED_BATTLE_TALK_SETTING = "enableDamagedBattleTalk";
    public static boolean enableDamagedBattleTalkEffect = true;

    public static final String DAMAGED_BATTLE_TALK_PROBABILITY_SETTING = "damagedTalkProbability";
    private static final int BASE_DAMAGED_TALK_PROBABILITY = 0;
    public static int damagedTalkProbability = BASE_DAMAGED_TALK_PROBABILITY; //Out of 100

    public static final String PRE_BATTLE_TALK_SETTING = "enablePreBattleTalk";
    public static boolean enablePreBattleTalkEffect = true;

    public static final String PRE_BATTLE_TALK_PROBABILITY_SETTING = "preTalkProbability";
    private static final int BASE_PRE_TALK_PROBABILITY = 0;
   public static int preTalkProbability = BASE_PRE_TALK_PROBABILITY; //Out of 100

    public static final String NORMA_NUMERATOR = "normaNumerator";
    public static final String NORMA_DENOMINATOR = "normaDenominator";
    public static final String NORMA_BASE = "normaBase";
    public static final String NORMA_SKILLS_ONLY = "normaSkillsOnly";


    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The M10 Robot";
    private static final String AUTHOR = "Mistress Alison";
    private static final String DESCRIPTION = "Adds M10 Robot (and cards) from 100% Orange Juice! NL See the Mod Options if you would like to change any configurations!";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    //public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    public static final Color GREEN_SPRING = CardHelper.getColor(185.0f, 197.0f, 180.0f);
    //public static final Color SHADOW = CardHelper.getColor(51.0f, 41.0f, 47.0f);
    //public static final Color VOID = CardHelper.getColor(149.0f, 147.0f, 150.0f);
    
    // Potion Colors in RGB

    public static final Color BURN_POTION_LIQUID = CardHelper.getColor(168, 56, 50); // Dark Red
    public static final Color BURN_POTION_HYBRID = CardHelper.getColor(207, 118, 35); // Lighter Orange
    public static final Color BURN_POTION_SPOTS = CardHelper.getColor(255, 102, 0); // Lighter Orange/Red

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    public static final String ATTACK_BASE = "M10RobotResources/images/512/bg_attack.png"; //
    public static final String SKILL_BASE = "M10RobotResources/images/512/bg_skill.png"; //
    public static final String POWER_BASE = "M10RobotResources/images/512/bg_power.png"; //

    public static final String ENERGY_ORB = "M10RobotResources/images/512/card_orb_green.png"; //

    public static final String CARD_ENERGY_ORB = "M10RobotResources/images/512/card_small_orb_green.png"; //

    public static final String ATTACK_BASE_PORTRAIT = "M10RobotResources/images/1024/bg_attack.png"; //
    public static final String SKILL_BASE_PORTRAIT = "M10RobotResources/images/1024/bg_skill.png"; //
    public static final String POWER_BASE_PORTRAIT = "M10RobotResources/images/1024/bg_power.png"; //

    public static final String ENERGY_ORB_PORTRAIT = "M10RobotResources/images/1024/card_orb_green.png"; //
    
    // Character assets
    //private static final String THE_DEFAULT_BUTTON = "M10RobotResources/images/charSelect/DefaultCharacterButton.png";
    private static final String M10_BUTTON = "M10RobotResources/images/charSelect/Button6.png"; //
    //private static final String THE_DEFAULT_PORTRAIT = "M10RobotResources/images/charSelect/DefaultCharacterPortraitBG.png";
    private static final String M10_BG = "M10RobotResources/images/charSelect/M10BG2.png";
    public static final String M10_SHOULDER_1 = "M10RobotResources/images/char/tachy/shoulder.png"; //
    public static final String M10_SHOULDER_2 = "M10RobotResources/images/char/tachy/shoulder2.png";
    public static final String M10_CORPSE = "M10RobotResources/images/char/tachy/KO_flip.png"; //
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "M10RobotResources/images/Badge.png";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public M10RobotMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("M10Robot");
        // cool
        // Done: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + M10Robot.Enums.GREEN_SPRING_CARD_COLOR.toString());
        
        BaseMod.addColor(M10Robot.Enums.GREEN_SPRING_CARD_COLOR, GREEN_SPRING, GREEN_SPRING, GREEN_SPRING,
                GREEN_SPRING, GREEN_SPRING, GREEN_SPRING, GREEN_SPRING,
                ATTACK_BASE, SKILL_BASE, POWER_BASE, ENERGY_ORB,
                ATTACK_BASE_PORTRAIT, SKILL_BASE_PORTRAIT, POWER_BASE_PORTRAIT,
                ENERGY_ORB_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theShadowSirenDefaultSettings.setProperty(ENABLE_SELFDAMAGE_SETTING, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableSelfDamage = config.getBool(ENABLE_SELFDAMAGE_SETTING);
            //enableStrongerWantedEffect = config.getBool(FIVE_STAR_WANTED_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(FIVE_STAR_WANTED_SETTING, "FALSE");
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableStrongerWantedEffect = config.getBool(FIVE_STAR_WANTED_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(DISABLE_GULL_VFX, "FALSE");
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            disableGullVfx = config.getBool(DISABLE_GULL_VFX);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(CARD_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableCardBattleTalkEffect = config.getBool(CARD_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(CARD_BATTLE_TALK_PROBABILITY_SETTING, String.valueOf(BASE_CARD_TALK_PROBABILITY));
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            cardTalkProbability = config.getInt(CARD_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(DAMAGED_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableDamagedBattleTalkEffect = config.getBool(DAMAGED_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING, String.valueOf(BASE_DAMAGED_TALK_PROBABILITY));
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            damagedTalkProbability = config.getInt(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(PRE_BATTLE_TALK_SETTING, "TRUE");
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePreBattleTalkEffect = config.getBool(PRE_BATTLE_TALK_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        theShadowSirenDefaultSettings.setProperty(PRE_BATTLE_TALK_PROBABILITY_SETTING, String.valueOf(BASE_PRE_TALK_PROBABILITY));
        try {
            SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            preTalkProbability = config.getInt(PRE_BATTLE_TALK_PROBABILITY_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done adding mod settings");

        normaBackupVars.setProperty(NORMA_NUMERATOR, String.valueOf(0));
        normaBackupVars.setProperty(NORMA_DENOMINATOR, String.valueOf(10));
        normaBackupVars.setProperty(NORMA_BASE, String.valueOf(0));
        normaBackupVars.setProperty(NORMA_SKILLS_ONLY, "FALSE");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = M10RobotMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = M10RobotMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = M10RobotMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing M10 Robot. =========================");
        M10RobotMod m10RobotMod = new M10RobotMod();
        logger.info("========================= /M10 Robot, Initialized/ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + M10Robot.Enums.THE_MIO_ROBOT.toString());
        
        BaseMod.addCharacter(new M10Robot("The M10 Robot", M10Robot.Enums.THE_MIO_ROBOT),
                M10_BUTTON, M10_BG, M10Robot.Enums.THE_MIO_ROBOT);
        
        receiveEditPotions();
        logger.info("Added " + M10Robot.Enums.THE_MIO_ROBOT.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        //Add WidePotion Compatibility
        if (Loader.isModLoaded("widepotions")) {

            logger.info("Wide Potions: Detected. Shenanigans: Engaged.");

            //Simple Potions

            WidePotionsMod.whitelistSimplePotion(BurnPotion.POTION_ID);

            //Complex Potions

            //WidePotionsMod.whitelistComplexPotion(MyOtherPotion.POTION_ID, new WideMyOtherPotion());
        }

        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        //Get the longest slider text for positioning
        ArrayList<String> labelStrings = new ArrayList<>();
        labelStrings.add(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigBattleTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigDamagedTalkButton")).TEXT[0]);
        labelStrings.add(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigPreBattleTalkButton")).TEXT[0]);
        float sliderOffset = getSliderPosition(labelStrings);
        labelStrings.clear();
        float currentYposition = 740f;
        float spacingY = 55f;
        
        // Create the on/off button:
        ModLabeledToggleButton enableSelfDamageButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigSeagulls")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableSelfDamage, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enableSelfDamage = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                config.setBool(ENABLE_SELFDAMAGE_SETTING, enableSelfDamage);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        currentYposition -= spacingY;
        ModLabeledToggleButton enableStrongerWantedButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigWanted")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableStrongerWantedEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableStrongerWantedEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setBool(FIVE_STAR_WANTED_SETTING, enableStrongerWantedEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        ModLabeledToggleButton disableGullVFXButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigGullVFXButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableGullVfx, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    disableGullVfx = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setBool(DISABLE_GULL_VFX, disableGullVfx);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when playing cards
        ModLabeledToggleButton enableCardBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigBattleTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableCardBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableCardBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setBool(CARD_BATTLE_TALK_SETTING, enableCardBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider cardBattleTalkSlider = new ModMinMaxSlider("",
                enableCardBattleTalkButton.getX() + sliderOffset,
                enableCardBattleTalkButton.getY() + 20f,
                0, 100, cardTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    cardTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setInt(CARD_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when taking damage
        ModLabeledToggleButton enableDamagedBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigDamagedTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableDamagedBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enableDamagedBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setBool(DAMAGED_BATTLE_TALK_SETTING, enableDamagedBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider damagedBattleTalkSlider = new ModMinMaxSlider("",
                enableDamagedBattleTalkButton.getX() + sliderOffset,
                enableDamagedBattleTalkButton.getY() + 20f,
                0, 100, damagedTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    damagedTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setInt(DAMAGED_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        currentYposition -= spacingY;
        //Used for randomly talking when combat starts and ends
        ModLabeledToggleButton enablePreBattleTalkButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("ModConfigPreBattleTalkButton")).TEXT[0],
                350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePreBattleTalkEffect, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enablePreBattleTalkEffect = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setBool(PRE_BATTLE_TALK_SETTING, enablePreBattleTalkEffect);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        ModMinMaxSlider preBattleTalkSlider = new ModMinMaxSlider("",
                enablePreBattleTalkButton.getX() + sliderOffset,
                enablePreBattleTalkButton.getY() + 20f,
                0, 100, preTalkProbability, "%.0f",
                settingsPanel,
                slider -> {
                    preTalkProbability = (int)slider.getValue();
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("M10Robot", "TimeTravellerConfig", theShadowSirenDefaultSettings);
                        config.setInt(PRE_BATTLE_TALK_PROBABILITY_SETTING, (int) slider.getValue());
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        //currentYposition -= spacingY;

        settingsPanel.addUIElement(enableSelfDamageButton); // Add the button to the settings panel. Button is a go.
        settingsPanel.addUIElement(enableStrongerWantedButton); // Add the button to the settings panel. Button is a go.
        settingsPanel.addUIElement(disableGullVFXButton);
        settingsPanel.addUIElement(enableCardBattleTalkButton);
        settingsPanel.addUIElement(cardBattleTalkSlider);
        settingsPanel.addUIElement(enableDamagedBattleTalkButton);
        settingsPanel.addUIElement(damagedBattleTalkSlider);
        settingsPanel.addUIElement(enablePreBattleTalkButton);
        settingsPanel.addUIElement(preBattleTalkSlider);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== SAVABLES =================
        logger.info("Preparing CustomSavables");



        // =============== /SAVABLES/ =================

        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"

        //No events have been made yet, dont add the default event, that would be silly
        //BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    //Get the longest text so all sliders are centered
    private float getSliderPosition (ArrayList<String> stringsToCompare) {
        float longest = 0;
        for (String s : stringsToCompare) {
            longest = Math.max(longest, FontHelper.getWidth(FontHelper.charDescFont, s, 1f / Settings.scale));
        }
        return longest + 60f;
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)

        BaseMod.addPotion(BurnPotion.class, BURN_POTION_LIQUID, BURN_POTION_HYBRID, BURN_POTION_SPOTS, BurnPotion.POTION_ID, M10Robot.Enums.THE_MIO_ROBOT);

        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new ProtectiveShell(), M10Robot.Enums.GREEN_SPRING_CARD_COLOR);
        //BaseMod.addRelicToCustomPool(new BottledStar(), M10Robot.Enums.GREEN_SPRING_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new ProtectiveShell2(), M10Robot.Enums.GREEN_SPRING_CARD_COLOR);
        BaseMod.addRelicToCustomPool(new ModularBody(), M10Robot.Enums.GREEN_SPRING_CARD_COLOR);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(ProtectiveShell.ID);
        //UnlockTracker.markRelicAsSeen(BottledStar.ID);
        UnlockTracker.markRelicAsSeen(ProtectiveShell2.ID);
        UnlockTracker.markRelicAsSeen(ModularBody.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new DefaultInvertedNumber());
        BaseMod.addDynamicVariable(new DynamicDynamicVariableManager());
        BaseMod.addDynamicVariable(new AmmoVariable());


        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards inside the cards package, found under yourModName.cards.
        // For more specific info, including how to exclude classes from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        new AutoAdd("M10Robot")
            .packageFilter("M10Robot.cards")
            .setDefaultSeen(true)
            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE LOCALIZATION ===================

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase()+ "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for " +fileName+".");
            return "eng" + "/" + fileName;
        } else {
            logger.info("Loaded Language: "+ Settings.language.toString().toLowerCase() + ", for "+fileName+".");
            return Settings.language.toString().toLowerCase() + "/" + fileName;
        }
    }

    // ================ /LOAD THE LOCALIZATION/ ===================

    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Card-Strings.json"));

        // CardModStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-CardMod-Strings.json"));

        // ChatterStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Chatter-Strings.json"));
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Power-Strings.json"));
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Relic-Strings.json"));
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Event-Strings.json"));
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Potion-Strings.json"));
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Character-Strings.json"));
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Orb-Strings.json"));

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-UI-Strings.json"));

        // Stance Strings
        BaseMod.loadCustomStringsFile(StanceStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Stance-Strings.json"));

        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID()+"Resources/localization/"+loadLocalizationIfAvailable("M10Robot-Keyword-Strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
