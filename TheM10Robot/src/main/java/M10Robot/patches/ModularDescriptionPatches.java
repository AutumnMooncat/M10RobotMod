package M10Robot.patches;

import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.stances.OnGainBlockStance;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class ModularDescriptionPatches {
    //CardModifiers all call initDesc when a mod is added or removed. We care about the description when this happens
    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class BeforeInitializeDescription {
        @SpirePrefixPatch
        public static void afterModApplied(AbstractCard __instance) {
            //Before we initialise our description, check if the card has a Modular description.
            if (__instance instanceof ModularDescription) {
                //If it does, call the change function to ensure we have the correct description
                ((ModularDescription) __instance).changeDescription();
            }
        }
    }

    /*//Patching applyPowers is needed if a power can alter the base values of our card in such a way that it needs an update
    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class AfterApplyPowers {
        @SpirePostfixPatch
        public static void afterModApplied(AbstractCard __instance) {
            if (__instance instanceof ModularDescription) {
                __instance.initializeDescription();
            }
        }
    }*/
}
