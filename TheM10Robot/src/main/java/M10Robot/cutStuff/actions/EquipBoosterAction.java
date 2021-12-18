//package M10Robot.cutStuff.actions;
//
//import M10Robot.cutStuff.AbstractBoosterCard;
//import M10Robot.patches.BoosterFieldPatch;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.vfx.combat.EmpowerEffect;
//
//public class EquipBoosterAction extends AbstractGameAction {
//    private static final float DURATION = 0.1F;
//    private final AbstractBoosterCard boosterCard;
//    private final AbstractCard targetCard;
//    private final AbstractPlayer p;
//    private final boolean automatedAddition;
//
//    /***
//     * Equips boosterCard onto targetCard. Should be used when manually right clicking a booster and choosing a target. The booster needs to be actively in your hand to work.
//     * @param targetCard - The card selected to have the booster applied to
//     * @param boosterCard - The booster card that will be applied
//     */
//    public EquipBoosterAction(AbstractCard targetCard, AbstractBoosterCard boosterCard) {
//        this(targetCard, boosterCard, false);
//    }
//
//    /***
//     * Equips boosterCard onto targetCard. If automatedAddition is set, the booster does NOT need to be in your hand to work. This should be used if a power/relic/etc. is adding the booster for you.
//     * @param targetCard The card selected to have the booster applied to
//     * @param boosterCard The booster card that will be applied
//     * @param automatedAddition If true, the booster does NOT need to be in your hand to be applied
//     */
//    public EquipBoosterAction(AbstractCard targetCard, AbstractBoosterCard boosterCard, boolean automatedAddition) {
//        this.boosterCard = boosterCard;
//        this.targetCard = targetCard;
//        p = AbstractDungeon.player;
//        this.actionType = ActionType.CARD_MANIPULATION;
//        this.duration = DURATION;
//        this.automatedAddition = automatedAddition;
//    }
//
//    public void update() {
//        //We use an in hand check in case we accidentally queue 2 equip actions via double clicking, the card wont be in the hand on the second action
//        //We use automated addition if a module/relic/power/etc. is adding a new booster without adding the booster to our hand
//        if (this.duration == DURATION && (p.hand.contains(boosterCard) || automatedAddition)) {
//            BoosterFieldPatch.betterEquipBooster(targetCard, boosterCard);
//            AbstractDungeon.player.hand.removeCard(boosterCard);
//            AbstractDungeon.player.onCardDrawOrDiscard();
//            this.addToTop(new AbstractGameAction() {
//                @Override
//                public void update() {
//                    AbstractDungeon.effectList.add(new EmpowerEffect(targetCard.hb.cX, targetCard.hb.cY));
//                    this.isDone = true;
//                }
//            });
//        }
//        this.tickDuration();
//    }
//}