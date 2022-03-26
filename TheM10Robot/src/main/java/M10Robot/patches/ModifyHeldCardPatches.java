//package M10Robot.patches;
//
//import M10Robot.cardModifiers.OverclockModifier;
//import M10Robot.cards.abstractCards.AbstractSwappableCard;
//import M10Robot.util.interfaces.OverclockBeforePlayItem;
//import M10Robot.util.OverclockUtil;
//import basemod.helpers.CardModifierManager;
//import com.evacipated.cardcrawl.modthespire.lib.SpireField;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.megacrit.cardcrawl.actions.utility.UseCardAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.cards.CardQueueItem;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//
//public class ModifyHeldCardPatches {
//    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
//    public static class ModifiedField {
//        public static SpireField<Boolean> didModification = new SpireField<>(() -> false);
//        public static SpireField<Integer> modAmount = new SpireField<>(() -> 0);
//        public static SpireField<Boolean> manuallyPlayed = new SpireField<>(() -> false);
//    }
//
//    public static int getOverclocks(AbstractCard card) {
//        int amount = 0;
//        for (AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof OverclockBeforePlayItem) {
//                amount += ((OverclockBeforePlayItem) p).overclockAmount(card);
//            }
//        }
//        for (AbstractRelic r : AbstractDungeon.player.relics) {
//            if (r instanceof OverclockBeforePlayItem) {
//                amount += ((OverclockBeforePlayItem) r).overclockAmount(card);
//            }
//        }
//        return amount;
//    }
//
//    public static void triggerOverclockers(AbstractCard card) {
//        for (AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof OverclockBeforePlayItem) {
//                ((OverclockBeforePlayItem) p).onOverclock(card);
//            }
//        }
//        for (AbstractRelic r : AbstractDungeon.player.relics) {
//            if (r instanceof OverclockBeforePlayItem) {
//                ((OverclockBeforePlayItem) r).onOverclock(card);
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "updateInput")
//    public static class ModifyHeldCard {
//        @SpirePostfixPatch
//        public static void modify(AbstractPlayer __instance) {
//            if (__instance.hoveredCard != null && ((__instance.isDraggingCard && __instance.isHoveringDropZone) || __instance.inSingleTargetMode)) {
//                if (!ModifiedField.didModification.get(__instance.hoveredCard) && OverclockUtil.canOverclock(__instance.hoveredCard)) {
//                    int amount = getOverclocks(__instance.hoveredCard);
//                    if (amount > 0) {
//                        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.1F);
//                        CardModifierManager.addModifier(__instance.hoveredCard, new OverclockModifier(amount));
//                        __instance.hoveredCard.superFlash();
//                        ModifiedField.didModification.set(__instance.hoveredCard, true);
//                        ModifiedField.modAmount.set(__instance.hoveredCard, amount);
//                    }
//                }
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "releaseCard")
//    public static class ResetDroppedCard {
//        @SpirePrefixPatch
//        public static void reset(AbstractPlayer __instance) {
//            if (__instance.hoveredCard != null && ModifiedField.didModification.get(__instance.hoveredCard)) {
//                if (CardModifierManager.hasModifier(__instance.hoveredCard, OverclockModifier.ID)) {
//                    OverclockModifier mod = ((OverclockModifier)CardModifierManager.getModifiers(__instance.hoveredCard, OverclockModifier.ID).get(0));
//                    if (mod.getRawPercent() > (ModifiedField.modAmount.get(__instance.hoveredCard) * OverclockModifier.PERCENT_PER_AMOUNT)) {
//                        CardModifierManager.addModifier(__instance.hoveredCard, new OverclockModifier(-ModifiedField.modAmount.get(__instance.hoveredCard)));
//                    } else {
//                        CardModifierManager.removeSpecificModifier(__instance.hoveredCard, mod, true);
//                    }
//                }
//                __instance.hoveredCard.applyPowers();
//                __instance.hoveredCard.initializeDescription();
//                ModifiedField.didModification.set(__instance.hoveredCard, false);
//                ModifiedField.modAmount.set(__instance.hoveredCard, 0);
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
//    public static class TriggersWhenCardPlayed {
//        @SpirePrefixPatch
//        public static void trigger(AbstractPlayer __instance) {
//            triggerOverclockers(__instance.hoveredCard);
//            ModifiedField.didModification.set(__instance.hoveredCard, false);
//            ModifiedField.modAmount.set(__instance.hoveredCard, 0);
//            ModifiedField.manuallyPlayed.set(__instance.hoveredCard, true);
//        }
//    }
//
//    @SpirePatch(clz = UseCardAction.class, method = "update")
//    public static class resetPlayerPlayed {
//        @SpirePostfixPatch
//        public static void reset(UseCardAction __instance, AbstractCard ___targetCard) {
//            if(__instance.isDone) {
//                ModifiedField.manuallyPlayed.set(___targetCard, false);
//            }
//        }
//    }
//
//    @SpirePatch(clz = CardQueueItem.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractMonster.class, int.class, boolean.class, boolean.class})
//    private static class BindObjectToDamageInfo {
//        @SpirePostfixPatch()
//        public static void overClockBeforeUsing(CardQueueItem __instance, AbstractCard card, AbstractMonster monster, int setEnergyOnUse, boolean ignoreEnergyTotal, boolean autoplayCard) {
//            if (card != null && !ModifiedField.manuallyPlayed.get(card) && OverclockUtil.canOverclock(card)) {
//                int amount = getOverclocks(card);
//                if (amount > 0) {
//                    CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.1F);
//                    CardModifierManager.addModifier(card, new OverclockModifier(amount));
//                    card.superFlash();
//                }
//                triggerOverclockers(card);
//            }
//        }
//    }
//}
