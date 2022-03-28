//package M10Robot.util;
//
//import M10Robot.cardModifiers.OverclockModifier;
//import M10Robot.cards.interfaces.CannotOverclock;
//import basemod.helpers.CardModifierManager;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.FontHelper;
//
//public class OverclockUtil {
//
//    public static boolean hasOverclock(AbstractCard c) {
//        return !CardModifierManager.getModifiers(c, OverclockModifier.ID).isEmpty();
//    }
//
//    public static int getOverClockPercent(AbstractCard c) {
//        return ((OverclockModifier) CardModifierManager.getModifiers(c, OverclockModifier.ID).get(0)).getRawPercent();
//    }
//
//    public static boolean canOverclock(AbstractCard c) {
//        return !(c instanceof CannotOverclock) && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE;
//    }
//
//    @SpirePatch(clz = AbstractCard.class, method = "renderTitle")
//    public static class renderOverclockPercent {
//        @SpirePostfixPatch
//        public static void renderPlz(AbstractCard __instance, SpriteBatch sb, Color ___renderColor) {
//            if (AbstractDungeon.player != null && hasOverclock(__instance)) {
//                Color color = Settings.GREEN_TEXT_COLOR.cpy();
//                color.a = ___renderColor.a;
//                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont, "+"+getOverClockPercent(__instance)+"%", __instance.current_x, __instance.current_y, 0.0F, 195.0F * __instance.drawScale * Settings.scale, __instance.angle, false, color);
//            }
//        }
//    }
//}
