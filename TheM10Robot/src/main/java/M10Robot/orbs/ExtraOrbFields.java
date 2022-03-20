package M10Robot.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class ExtraOrbFields {
    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class ExtraFields {
        public static SpireField<Integer> timesUpgraded = new SpireField<>(() -> 0);
        public static SpireField<String> baseName = new SpireField<>(() -> "");
        public static SpireField<Integer> passiveIncrease = new SpireField<>(() -> -1);
        public static SpireField<Integer> evokeIncrease = new SpireField<>(() -> -1);
    }

    public static void upgradeCount(AbstractOrb o) {
        upgradeCount(o, 1);
    }

    public static void upgradeCount(AbstractOrb o, int times) {
        ExtraFields.timesUpgraded.set(o, ExtraFields.timesUpgraded.get(o)+times);
    }
}
