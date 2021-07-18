package M10Robot.cards.abstractCards;

import M10Robot.M10RobotMod;
import M10Robot.actions.EquipBoosterAction;
import M10Robot.actions.SelectCardsForBoosterAction;
import M10Robot.cardModifiers.AbstractBoosterModifier;
import M10Robot.cards.BlankSlate;
import M10Robot.cards.modules.Repeater;
import M10Robot.patches.BoosterFieldPatch;
import M10Robot.patches.BypassEnergyPatches;
import M10Robot.patches.HandCardSelectScreenPatches;
import M10Robot.patches.TypeOverridePatch;
import M10Robot.predicates.SwappableAttentivePredicate;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
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
    private SelectCardsForBoosterAction action;
    private boolean madeAttempt;

    protected Predicate<AbstractCard> isPlayable = this::checkPlayability; //All reloadable cards are playable, but only if they have ammo, so we do a special check
    protected Predicate<AbstractCard> hasBlockValue = c -> (c.baseBlock >= 0 && !(c instanceof RitualDagger)) || BoosterFieldPatch.hasBlockGainingBooster(c);
    protected Predicate<AbstractCard> hasDamageValue = c -> c.baseDamage >= 0 || BoosterFieldPatch.hasDamageDealingBooster(c);
    protected Predicate<AbstractCard> hasMagicValue = c -> c.baseMagicNumber >= 0 || BoosterFieldPatch.hasMagicUtilizingBooster(c);
    protected Predicate<AbstractCard> isAttack = c -> c.type == CardType.ATTACK || c instanceof Repeater || c instanceof BlankSlate;
    protected Predicate<AbstractCard> isSkill = c -> c.type == CardType.SKILL && !(c instanceof AbstractBoosterCard);
    protected Predicate<AbstractCard> isPower = c -> c.type == CardType.POWER && !(c instanceof AbstractModuleCard);
    protected Predicate<AbstractCard> isModule = c -> c instanceof AbstractModuleCard;
    protected Predicate<AbstractCard> nonZeroCost = c -> c.cost > 0 || c.cost == -1;

    public AbstractBoosterCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, -2, type, color, rarity, target);
        TypeOverridePatch.setOverride(this, BaseMod.getKeywordTitle("m10robot:booster"));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return false;
    }

    public boolean checkPlayability(AbstractCard c) {
        //Repeater has special logic and counts as playable
        if (c instanceof Repeater) {
            return true;
        }
        boolean backup = BypassEnergyPatches.BypassEnergyCheckField.bypass.get(c);
        BypassEnergyPatches.BypassEnergyCheckField.bypass.set(c, true);
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                if (c.canUse(AbstractDungeon.player, aM)) {
                    BypassEnergyPatches.BypassEnergyCheckField.bypass.set(c, backup);
                    return true;
                }
            }
        }
        BypassEnergyPatches.BypassEnergyCheckField.bypass.set(c, backup);
        return false;
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

    @Override
    public void onRightClick() {
        if (!madeAttempt && action == null) {
            madeAttempt = true;
            Consumer<List<AbstractCard>> plsWork = l -> l.forEach(this::onEquip);
            SwappableAttentivePredicate pre = new SwappableAttentivePredicate(getFilter().and(BoosterFieldPatch::canEquipBooster));
            HandCardSelectScreenPatches.PreviewWithBoosterField.previewBooster.set(AbstractDungeon.handCardSelectScreen, true);
            HandCardSelectScreenPatches.PreviewWithBoosterField.booster.set(AbstractDungeon.handCardSelectScreen, this);
            action = new SelectCardsForBoosterAction(1, CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT[0], true, true, pre::testPredicatesOnSwappable, plsWork);
            this.addToTop(action);
        }
    }

    public abstract Predicate<AbstractCard> getFilter();

    public void onEquip(AbstractCard card) {
        madeAttempt = false;
        this.addToTop(new EquipBoosterAction(card, this));
    }

    public abstract ArrayList<AbstractBoosterModifier> getBoosterModifiers();

    @Override
    public void update() {
        super.update();
        if (action != null && action.isDone) {
            HandCardSelectScreenPatches.PreviewWithBoosterField.previewBooster.set(AbstractDungeon.handCardSelectScreen, false);
            HandCardSelectScreenPatches.PreviewWithBoosterField.booster.set(AbstractDungeon.handCardSelectScreen, null);
            if (madeAttempt && !action.screenWasOpened) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT[1], true));
            }
            madeAttempt = false;
            action = null;
        }
    }
}
