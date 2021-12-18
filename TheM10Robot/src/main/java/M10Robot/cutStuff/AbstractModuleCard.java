//package M10Robot.cutStuff;
//
//
//import M10Robot.cutStuff.actions.EquipModuleAction;
//import M10Robot.cards.abstractCards.AbstractClickableCard;
//import M10Robot.patches.TypeOverridePatch;
//import basemod.BaseMod;
//import basemod.helpers.TooltipInfo;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class AbstractModuleCard extends AbstractClickableCard {
//
//    private static ArrayList<TooltipInfo> moduleTooltip;
//
//    public AbstractModuleCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
//        super(id, img, -2, type, color, rarity, target);
//        TypeOverridePatch.setOverride(this, BaseMod.getKeywordTitle("m10robot:module"));
//    }
//
//    @Override
//    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        return false;
//    }
//
//    @Override
//    public boolean cardPlayable(AbstractMonster m) {
//        return false;
//    }
//
//    /*public List<String> getCardDescriptors() {
//        List<String> tags = new ArrayList<>();
//        tags.add(BaseMod.getKeywordTitle("m10robot:module"));
//        tags.addAll(super.getCardDescriptors());
//        return tags;
//    }*/
//
//    @Override
//    public List<TooltipInfo> getCustomTooltipsTop() {
//        if (moduleTooltip == null)
//        {
//            moduleTooltip = new ArrayList<>();
//            moduleTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("m10robot:module"), BaseMod.getKeywordDescription("m10robot:module")));
//        }
//        List<TooltipInfo> compoundList = new ArrayList<>(moduleTooltip);
//        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
//        return compoundList;
//    }
//
//    public void onRightClick() {
//        this.addToTop(new EquipModuleAction(this));
//    }
//
//    public void onEquip() {}
//
//    public void onRemove() {};
//}
