package M10Robot.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class EchoFields {
    public static SpireField<Integer> echo = new SpireField<>(() -> 0);
    public static SpireField<Integer> baseEcho = new SpireField<>(() -> 0);
    public static SpireField<Boolean> isEchoUpgraded = new SpireField<>(() -> false);
}
