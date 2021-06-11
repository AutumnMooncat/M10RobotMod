package M10Robot.patches;

import M10Robot.stances.OnGainBlockStance;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class OnApplyPowersToBlockPatch {

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class DamageTakenListener {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void damageTakenListener(AbstractCard __instance, @ByRef float[] tmp) {
            if (AbstractDungeon.player.stance instanceof OnGainBlockStance) {
                tmp[0] = ((OnGainBlockStance) AbstractDungeon.player.stance).onGainBlock(tmp[0]);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
