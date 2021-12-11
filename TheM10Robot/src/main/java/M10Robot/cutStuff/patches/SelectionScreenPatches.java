package M10Robot.cutStuff.patches;
/*
import M10Robot.actions.SelectCardsForBoosterAction;
import M10Robot.cutStuff.stances.OnGainBlockStance;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CtBehavior;

public class SelectionScreenPatches {
    @SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class SelectionFields {
        public static SpireField<Boolean> selectionWasOpened = new SpireField<>(() -> Boolean.FALSE);
    }

    public static boolean wasSelectionScreenOpened(AbstractGameAction a) {
        return SelectionFields.selectionWasOpened.get(a);
    }

    @SpirePatch(clz = SelectCardsForBoosterAction.class, method = "update")
    public static class DidTheScreenOpen {
        @SpireInsertPatch(locator = Locator.class)
        public static void didItOpen(SelectCardsForBoosterAction __instance) {
            SelectionFields.selectionWasOpened.set(__instance, true);
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(HandCardSelectScreen.class, "open");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
*/