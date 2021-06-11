package M10Robot.patches;
/*
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YesNoFormatPatch {

    private static String processString(String str) {
        if (str != null) {
            ArrayList<String> numbers = new ArrayList<>();
            //Pattern p = Pattern.compile("\\d+");
            Pattern p = Pattern.compile("(?!\\S+?[:]\\S)\\d+");
            Matcher m = p.matcher(str);
            while (m.find()) {
                numbers.add(m.group());
            }
            for (String s : numbers) {
                if (NumberUtils.isCreatable(s)) {
                    Number n = NumberUtils.createNumber(s);
                    if (n.intValue() > 0) {
                        str = str.replace(s, "YES");
                    } else {
                        str = str.replace(s, "NO");
                    }
                }
            }
        }
        return str;
    }

//    private static String processFoxString(String str) {
//        if (str != null) {
//            String[] words = str.split(" ");
//            for (String s : words) {
//                str = str.replace(s, "Fox");
//            }
//        }
//        return str;
//    }

    @SpirePatch(clz = BitmapFont.class, method = "draw", paramtypez = {Batch.class, CharSequence.class, float.class, float.class})
    public static class RuinAllFormattingPls1 {
        public static void Prefix(BitmapFont __instance, Batch batch, @ByRef CharSequence[] str, float x, float y) {
            str[0] = processString((String) str[0]);
        }
    }

    @SpirePatch(clz = BitmapFont.class, method = "draw", paramtypez = {Batch.class, CharSequence.class, float.class, float.class, float.class, int.class, boolean.class})
    public static class RuinAllFormattingPls2 {
        public static void Prefix(BitmapFont __instance, Batch batch, @ByRef CharSequence[] str, float x, float y, @ByRef float[] targetWidth, int halign, boolean wrap) {
            float before = str[0].length();
            str[0] = processString((String) str[0]);
            float after = str[0].length();
            targetWidth[0] *= (before/after);
        }
    }

    @SpirePatch(clz = BitmapFont.class, method = "draw", paramtypez = {Batch.class, CharSequence.class, float.class, float.class, int.class, int.class, float.class, int.class, boolean.class})
    public static class RuinAllFormattingPls3 {
        public static void Prefix(BitmapFont __instance, Batch batch, @ByRef CharSequence[] str, float x, float y, int start, int end, @ByRef float[] targetWidth, int halign, boolean wrap) {
            float before = str[0].length();
            str[0] = processString((String) str[0]);
            float after = str[0].length();
            targetWidth[0] *= (before/after);
        }
    }

    @SpirePatch(clz = BitmapFont.class, method = "draw", paramtypez = {Batch.class, CharSequence.class, float.class, float.class, int.class, int.class, float.class, int.class, boolean.class, String.class})
    public static class RuinAllFormattingPls4 {
        public static void Prefix(BitmapFont __instance, Batch batch, @ByRef CharSequence[] str, float x, float y, int start, int end, @ByRef float[] targetWidth, int halign, boolean wrap, String truncate) {
            float before = str[0].length();
            str[0] = processString((String) str[0]);
            float after = str[0].length();
            targetWidth[0] *= (before/after);
        }
    }

    @SpirePatch(clz = FontHelper.class, method = "renderSmartText", paramtypez = {SpriteBatch.class, BitmapFont.class, String.class, float.class, float.class, float.class, float.class, Color.class})
    public static class DontMessUpSpacingPls {
        @SpirePrefixPatch
        public static void pls(SpriteBatch sb, BitmapFont font, @ByRef String[] msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor) {
            msg[0] = processString(msg[0]);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderDynamicVariable")
    public static class FixVariablesPls {
        @SpireInsertPatch(locator = Locator.class, localvars = {"num"})
        public static void pls(AbstractCard __instance, char key, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character end, @ByRef StringBuilder[] ___sbuilder, int num) {
            ___sbuilder[0].setLength(0);
            ___sbuilder[0].append(processString(Integer.toString(num)));
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                com.evacipated.cardcrawl.modthespire.lib.Matcher finalMatcher = new com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCustomDynamicVariable.Inner.class, method = "myRenderDynamicVariable")
    @SpirePatch(clz = basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.RenderCustomDynamicVariable.Inner.class, method = "myRenderDynamicVariable")
    public static class FixVariablesForModdedCardsPls {
        @SpireInsertPatch(locator = Locator.class, localvars = {"stringBuilder","num"})
        public static void pls(Object __obj_instance, String key, char ckey, float start_x, float draw_y, int i, BitmapFont font, SpriteBatch sb, Character cend, @ByRef StringBuilder[] stringBuilder, int num) {
            stringBuilder[0].setLength(0);
            stringBuilder[0].append(processString(Integer.toString(num)));
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                com.evacipated.cardcrawl.modthespire.lib.Matcher finalMatcher = new com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher(GlyphLayout.class, "setText");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static class fixHardcodedNumbersPls {
        static String backupText = "";
        @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
        public static class DontMessUpSpacingPls {
            @SpirePrefixPatch
            public static void pls(AbstractCard __instance) {
                backupText = __instance.rawDescription;
                __instance.rawDescription = processString(__instance.rawDescription);
            }
        }
        @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
        public static class BackToNormal {
            @SpirePostfixPatch
            public static void pls(AbstractCard __instance) {
                __instance.rawDescription = backupText;
            }
        }
    }
}
*/