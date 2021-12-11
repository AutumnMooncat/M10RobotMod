package M10Robot.cutStuff.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class CostBypassField {
    public static SpireField<Boolean> bypassCost = new SpireField<>(() -> false);
}

