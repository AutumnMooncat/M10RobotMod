package M10Robot.patches;

import M10Robot.cardModifiers.AbstractBoosterModifier;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class BypassEnergyPatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class BypassEnergyCheckField {
        public static SpireField<Boolean> bypass = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class BypassEnergyCheck {
        @SpirePrefixPatch
        public static SpireReturn<?> bypass(AbstractCard __instance) {
            if (BypassEnergyCheckField.bypass.get(__instance)) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            BypassEnergyCheckField.bypass.set(result, BypassEnergyCheckField.bypass.get(self));
            return result;
        }
    }
}
