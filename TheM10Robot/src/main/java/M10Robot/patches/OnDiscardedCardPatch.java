package M10Robot.patches;

import M10Robot.cards.interfaces.OnDiscardedCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CtBehavior;

public class OnDiscardedCardPatch {
    @SpirePatch2(clz = DiscardAction.class, method = "update")
    public static class DiscardActionHook {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void onDiscard(DiscardAction __instance, AbstractCard c, boolean ___endTurn) {
            if (c instanceof OnDiscardedCard) {
                ((OnDiscardedCard) c).onDiscarded(___endTurn);
            }
        }
    }

    public static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
            return LineFinder.findAllInOrder(ctBehavior, matcher);
        }
    }
}
