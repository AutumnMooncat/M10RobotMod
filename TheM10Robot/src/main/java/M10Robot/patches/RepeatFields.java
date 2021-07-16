package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = "<class>"
)
public class RepeatFields {
    public static SpireField<Integer> repeat = new SpireField<>(() -> 0);
    public static SpireField<Integer> baseRepeat = new SpireField<>(() -> 0);
    public static SpireField<Boolean> isRepeatUpgraded = new SpireField<>(() -> false);
}
