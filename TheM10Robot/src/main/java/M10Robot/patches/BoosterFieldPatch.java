package M10Robot.patches;

import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.cardModifiers.DealDamageEffect;
import M10Robot.cardModifiers.DrawCardEffect;
import M10Robot.cardModifiers.GainBlockEffect;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import M10Robot.cards.abstractCards.AbstractFrameworkCard;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BoosterFieldPatch {

    private static final Color boosterColor = Color.valueOf("65ada1");
    private static final Color boosterUpgradeColor = Color.valueOf("c26ad4");

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class BoosterFields {
        public static SpireField<Integer> boostersEquipped = new SpireField<>(() -> 0);
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
            if (mod instanceof DealDamageEffect) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMagicUtilizingBooster(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof DrawCardEffect) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEquipBooster(AbstractCard card) {
        return !(card instanceof AbstractBoosterCard) && !(card instanceof AbstractFrameworkCard);
    }

    public static void equipBooster(AbstractCard card) {
        BoosterFields.boostersEquipped.set(card, BoosterFields.boostersEquipped.get(card) + 1);
    }

    public static void unequipBooster(AbstractCard card) {
        BoosterFields.boostersEquipped.set(card, BoosterFields.boostersEquipped.get(card) - 1);
    }

    public static boolean hasBoosterEquipped(AbstractCard card) {
        return BoosterFields.boostersEquipped.get(card) > 0;
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
            BoosterFields.boostersEquipped.set(result, BoosterFields.boostersEquipped.get(self));
            return result;
        }
    }
}
