package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class RepeatFieldPatches {

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            RepeatFields.repeat.set(result, RepeatFields.repeat.get(self));
            RepeatFields.baseRepeat.set(result, RepeatFields.baseRepeat.get(self));
            RepeatFields.isRepeatUpgraded.set(result, RepeatFields.isRepeatUpgraded.get(self));
            return result;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class DamageTakenListener {
        @SpireInsertPatch(locator = Locator.class)
        public static void damageTakenListener(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (RepeatFields.repeat.get(c) > 0) {
                for (int i = 0 ; i < RepeatFields.repeat.get(c) ; i++) {
                    c.use(__instance, monster);
                    AbstractDungeon.actionManager.addToBottom(new UseCardAction(c, monster));
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "use");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
