package M10Robot.patches;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.*;
import M10Robot.cardModifiers.interfaces.RequiresSingleTargetAimingMode;
import M10Robot.cards.BlankSlate;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.cards.abstractCards.AbstractFrameworkCard;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class BoosterFieldPatch {

    public static final Color boosterColor = Color.valueOf("65ada1");
    public static final Color boosterUpgradeColor = Color.valueOf("c26ad4");
    private static final String augmentedKeyword = M10RobotMod.makeID("Augmented");

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class BoosterFields {
        public static SpireField<Integer> boostersEquipped = new SpireField<>(() -> 0);
        public static SpireField<CardGroup> equippedBoosters = new SpireField<>(() -> new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
        public static SpireField<HashMap<AbstractCard, ArrayList<AbstractBoosterModifier>>> linkedModifiers = new SpireField<>(HashMap::new);
        public static SpireField<AbstractCard.CardTarget> previousCardTarget = new SpireField<>(() -> null);
    }

    public static boolean hasBlockGainingBooster(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof GainBlockEffect) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasDamageDealingBooster(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof DealDamageEffect || mod instanceof DealDamageToAllEnemiesEffect) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMagicUtilizingBooster(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractSimpleStackingExtraEffectModifier) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEquipBooster(AbstractCard card) {
        return !(card instanceof AbstractBoosterCard) && !(card instanceof AbstractFrameworkCard);
    }

    public static void betterEquipBooster(AbstractCard targetCard, AbstractBoosterCard boosterCard) {
        //Add the booster if it isn't already there. Its possible that it IS already there if something messed up
        if (!BoosterFields.equippedBoosters.get(targetCard).contains(boosterCard)) {
            BoosterFields.equippedBoosters.get(targetCard).addToTop(boosterCard);
        }
        //Set our size to the number of boosters equipped
        BoosterFields.boostersEquipped.set(targetCard, BoosterFields.equippedBoosters.get(targetCard).size());
        //The linked modifiers is a hashmap, so we don't need to worry about it getting called twice hopefully

        //We do need to know if the card is Blank Slate, so we can double the modifiers
        ArrayList<AbstractBoosterModifier> mods = boosterCard.getBoosterModifiers();
        if (targetCard instanceof BlankSlate) {
            //We keep this as a separate array in case one or more swappable cards is Blank Slate, and we don't want to copy twice the mods when we shouldn't
            ArrayList<AbstractBoosterModifier> doubleMods = new ArrayList<>();
            for (AbstractBoosterModifier mod : mods) {
                doubleMods.add(mod);
                doubleMods.add((AbstractBoosterModifier) mod.makeCopy());
            }
            for (AbstractBoosterModifier mod : doubleMods) {
                checkModBeforeAdding(targetCard, mod.makeCopy());
            }
            BoosterFields.linkedModifiers.get(targetCard).put(boosterCard, doubleMods);
        } else {
            for (AbstractBoosterModifier mod : mods) {
                checkModBeforeAdding(targetCard, mod.makeCopy());
            }
            BoosterFields.linkedModifiers.get(targetCard).put(boosterCard, mods);
        }

        //Same dice for the swappable card
        if (targetCard instanceof AbstractSwappableCard && targetCard.cardsToPreview != null) {
            if (!BoosterFields.equippedBoosters.get(targetCard.cardsToPreview).contains(boosterCard)) {
                BoosterFields.equippedBoosters.get(targetCard.cardsToPreview).addToTop(boosterCard);
            }
            BoosterFields.boostersEquipped.set(targetCard.cardsToPreview, BoosterFields.equippedBoosters.get(targetCard.cardsToPreview).size());
            BoosterFields.linkedModifiers.get(targetCard.cardsToPreview).put(boosterCard, boosterCard.getBoosterModifiers());
            //Slightly different, we need a copy of the card mods for our swappable card
            ArrayList<AbstractBoosterModifier> copyMods = new ArrayList<>();
            for (AbstractBoosterModifier mod : mods) {
                copyMods.add((AbstractBoosterModifier) mod.makeCopy());
            }
            for (AbstractBoosterModifier mod : copyMods) {
                checkModBeforeAdding(targetCard.cardsToPreview, mod.makeCopy());
            }
            BoosterFields.linkedModifiers.get(targetCard.cardsToPreview).put(boosterCard, copyMods);
        }
        targetCard.applyPowers();
        targetCard.superFlash();
        if (targetCard instanceof AbstractSwappableCard && targetCard.cardsToPreview != null) {
            targetCard.cardsToPreview.applyPowers();
        }
    }

    private static void checkModBeforeAdding(AbstractCard targetCard, AbstractCardModifier mod) {
        if (mod instanceof RequiresSingleTargetAimingMode && BoosterFields.previousCardTarget.get(targetCard) == null) {
            if (targetCard.target != AbstractCard.CardTarget.ENEMY && targetCard.target != AbstractCard.CardTarget.SELF_AND_ENEMY) {
                BoosterFields.previousCardTarget.set(targetCard, targetCard.target);
                if (targetCard.target == AbstractCard.CardTarget.SELF) {
                    targetCard.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
                } else {
                    targetCard.target = AbstractCard.CardTarget.ENEMY;
                }
            }
        }
        CardModifierManager.addModifier(targetCard, mod);
    }

    private static void checkModAfterRemoving(AbstractCard targetCard, AbstractCardModifier mod) {
        if (mod instanceof RequiresSingleTargetAimingMode && BoosterFields.previousCardTarget.get(targetCard) != null) {
            boolean shouldReset = true;
            for (ArrayList<AbstractBoosterModifier> l : BoosterFields.linkedModifiers.get(targetCard).values()) {
                for (AbstractCardModifier m : l) {
                    if (m instanceof RequiresSingleTargetAimingMode) {
                        shouldReset = false;
                        break;
                    }
                }
            }
            if (shouldReset) {
                targetCard.target = BoosterFields.previousCardTarget.get(targetCard);
                BoosterFields.previousCardTarget.set(targetCard, null);
            }
        }
    }

    public static void unequipBooster(AbstractCard card, AbstractBoosterCard booster) {
        if (!BoundFieldPatch.boundField.get(booster)) {
            BoosterFields.equippedBoosters.get(card).removeCard(booster);
            BoosterFields.boostersEquipped.set(card, BoosterFields.equippedBoosters.get(card).size());
            unstackBoosters(card, booster);
            BoosterFields.linkedModifiers.get(card).remove(booster);
            if (card instanceof AbstractSwappableCard && card.cardsToPreview != null) {
                BoosterFields.equippedBoosters.get(card.cardsToPreview).removeCard(booster);
                BoosterFields.boostersEquipped.set(card.cardsToPreview, BoosterFields.equippedBoosters.get(card.cardsToPreview).size());
                unstackBoosters(card.cardsToPreview, booster);
                BoosterFields.linkedModifiers.get(card.cardsToPreview).remove(booster);
            }
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            temp.addToTop(booster);
            temp.moveToDiscardPile(booster);
            /*if (BoosterFields.boostersEquipped.get(card) == 0) {
                card.keywords.remove(augmentedKeyword);
            }*/
        }
    }

    public static void unstackBoosters(AbstractCard card, AbstractBoosterCard booster) {
        if (BoosterFields.linkedModifiers.get(card) != null) {
            if (BoosterFields.linkedModifiers.get(card).get(booster) != null) {
                for (AbstractBoosterModifier linkedMod : BoosterFields.linkedModifiers.get(card).get(booster)) {
                    if (linkedMod != null) {
                        if (linkedMod.unstack(card, linkedMod.getAmount())) {
                            checkModAfterRemoving(card, linkedMod);
                        }
                    }
                }
            }
        }
    }

    public static boolean hasBoosterEquipped(AbstractCard card) {
        return BoosterFields.boostersEquipped.get(card) > 0;
    }

    public static CardGroup getEquippedBoosters(AbstractCard card) {
        return BoosterFields.equippedBoosters.get(card);
    }

    public static String getBoosterPrefixes(AbstractCard card) {
        StringBuilder sb = new StringBuilder();
        /*for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractBoosterModifier) {
                sb.append(((AbstractBoosterModifier) mod).getPrefix());
            }
        }
        sb.append(" ");*/
        return sb.toString();
    }

    public static String getBoosterSuffixes(AbstractCard card) {
        StringBuilder sb = new StringBuilder();
        /*sb.append(" ");
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractBoosterModifier) {
                sb.append(((AbstractBoosterModifier) mod).getSuffix());
            }
        }*/
        return sb.toString();
    }

    //Is actually used by DynamicNameModificationPatches
    public static Color getCardTitleBoosterColor(AbstractCard card) {
        if (card.upgraded) {
            return boosterUpgradeColor.cpy();
        } else {
            return boosterColor.cpy();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            HashMap<AbstractCard, ArrayList<AbstractBoosterModifier>> map = new HashMap<>();
            for (AbstractCard c : BoosterFields.equippedBoosters.get(self).group) {
                ArrayList<AbstractBoosterModifier> mods = new ArrayList<>();
                for (AbstractBoosterModifier mod : BoosterFields.linkedModifiers.get(self).get(c)) {
                    mods.add((AbstractBoosterModifier) mod.makeCopy());
                }
                group.addToTop(c);
                map.put(c, mods);
            }
            BoosterFields.equippedBoosters.set(result, group);
            BoosterFields.boostersEquipped.set(result, BoosterFields.equippedBoosters.get(result).size());
            BoosterFields.linkedModifiers.set(result, map);
            return result;
        }
    }
}
