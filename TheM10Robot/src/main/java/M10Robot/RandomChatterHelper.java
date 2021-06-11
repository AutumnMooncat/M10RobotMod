package M10Robot;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class RandomChatterHelper {

    private static final CardStrings AttackTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("AttackTextContainer"));

    private static final CardStrings SkillTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("SkillTextContainer"));

    private static final CardStrings PowerTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("PowerTextContainer"));

    private static final CardStrings BattleStartTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("BattleStartTextContainer"));

    private static final CardStrings LowHPBattleStartTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("LowHPBattleStartTextContainer"));

    private static final CardStrings BattleEndTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("BattleEndTextContainer"));

    private static final CardStrings BossFightTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("BossFightTextContainer"));

    private static final CardStrings HealingTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("HealingTextContainer"));

    private static final CardStrings FieldDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("FieldDamageTextContainer"));

    private static final CardStrings LightDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("LightDamageTextContainer"));

    private static final CardStrings HeavyDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("HeavyDamageTextContainer"));

    private static final CardStrings BlockedDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("BlockedDamageTextContainer"));

    private static final CardStrings KOTextContainer = CardCrawlGame.languagePack.getCardStrings(M10RobotMod.makeID("KOTextContainer"));

    public static String getAttackText() {
        return AttackTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, AttackTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getSkillText() {
        return SkillTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, SkillTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getPowerText() {
        return PowerTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, PowerTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBattleStartText() {
        return BattleStartTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BattleStartTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getLowHPBattleStartText() {
        return LowHPBattleStartTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, LowHPBattleStartTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBattleEndText() {
        return BattleEndTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BattleEndTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBossFightText() {
        return BossFightTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BossFightTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getHealingText() {
        return HealingTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, HealingTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getFieldDamageText() {
        return FieldDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, FieldDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getLightDamageText() {
        return LightDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, LightDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getHeavyDamageText() {
        return HeavyDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, HeavyDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBlockedDamageText() {
        return BlockedDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BlockedDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getKOText() {
        return KOTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, KOTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    /**
     *
     * @param chatterText - The string to say if we show text
     * @param probability - The chance we actually talk
     * @param conditional - If we actually will show text or not. Mainly used for config stuff
     * @return - Returns true if we talked or false if we did not. Can be used to have additional conditionals on chat
     */
    public static boolean showChatter(String chatterText, int probability, boolean conditional) {
        if(conditional && MathUtils.random(1, 100) <= probability) {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, chatterText, true));
            //AbstractDungeon.actionManager.addToTop(new TalkAction(true, chatterText, 0.0f, 2.0f));
            return true;
        }
        return false;
    }
}
