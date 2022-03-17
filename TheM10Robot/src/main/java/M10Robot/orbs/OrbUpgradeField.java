package M10Robot.orbs;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class OrbUpgradeField {
    @SpirePatch(clz = AbstractOrb.class, method = SpirePatch.CLASS)
    public static class UpgradeCount {
        public static SpireField<Integer> timesUpgraded = new SpireField<>(() -> 0);
    }

    public static void upgradeCount(AbstractOrb o) {
        upgradeCount(o, 1);
    }

    public static void upgradeCount(AbstractOrb o, int times) {
        OrbUpgradeField.UpgradeCount.timesUpgraded.set(o, OrbUpgradeField.UpgradeCount.timesUpgraded.get(o)+times);
    }
}
