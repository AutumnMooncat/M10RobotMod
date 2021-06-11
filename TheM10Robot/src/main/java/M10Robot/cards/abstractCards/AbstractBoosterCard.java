package M10Robot.cards.abstractCards;

import M10Robot.M10RobotMod;
import M10Robot.actions.EquipBoosterAction;
import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.patches.BoosterFieldPatch;
import M10Robot.predicates.SwappableAttentivePredicate;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractBoosterCard extends AbstractClickableCard {

    private static ArrayList<TooltipInfo> boosterTooltip;
    private AbstractGameAction action;
    private boolean madeAttempt;

    protected Predicate<AbstractCard> isPlayable = c -> c.canUse(AbstractDungeon.player, null) || c instanceof AbstractReloadableCard; //All reloadable cards are playable, but only if they have ammo, so we do an or check
    protected Predicate<AbstractCard> hasBlockValue = c -> (c.baseBlock > 0 && !(c instanceof RitualDagger)) || BoosterFieldPatch.hasBlockGainingBooster(c);
    protected Predicate<AbstractCard> hasDamageValue = c -> c.baseDamage > 0 || BoosterFieldPatch.hasDamageDealingBooster(c);
    protected Predicate<AbstractCard> hasMagicValue = c -> c.baseMagicNumber > 0 || BoosterFieldPatch.hasMagicUtilizingBooster(c);

    public AbstractBoosterCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, -2, type, color, rarity, target);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("m10robot:booster"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (boosterTooltip == null)
        {
            boosterTooltip = new ArrayList<>();
            boosterTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("m10robot:booster"), BaseMod.getKeywordDescription("m10robot:booster")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(boosterTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    /*@Override
    public void applyPowers() {}

    @Override
    public void calculateCardDamage(AbstractMonster mo) {}*/

    @Override
    public void onRightClick() {
        if (!madeAttempt && action == null) {
            madeAttempt = true;
            Consumer<List<AbstractCard>> plsWork = l -> l.forEach(this::onEquip);
            SwappableAttentivePredicate pre = new SwappableAttentivePredicate(getFilter().and(BoosterFieldPatch::canEquipBooster));
            //action = new SelectCardsInHandAction(1, CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT[0], true, true, getFilter().and(BoosterFieldPatch::canEquipBooster), plsWork);
            action = new SelectCardsInHandAction(1, CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT[0], true, true, pre::testPredicatesOnSwappable, plsWork);
            this.addToTop(action);
        }
    }

    public abstract Predicate<AbstractCard> getFilter();

    public void onEquip(AbstractCard card) {
        madeAttempt = false;
        this.addToTop(new EquipBoosterAction(this, card));
    }

    public abstract ArrayList<AbstractBoosterModifier> getBoosterModifiers();

    @Override
    public void update() {
        super.update();
        if (action != null && action.isDone) {
            if (madeAttempt) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT[1], true));
            }
            madeAttempt = false;
            action = null;
        }
    }
}
