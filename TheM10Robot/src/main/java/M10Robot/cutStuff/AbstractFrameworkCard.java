package M10Robot.cutStuff;

import M10Robot.cards.abstractCards.AbstractClickableCard;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFrameworkCard extends AbstractClickableCard {

    private static ArrayList<TooltipInfo> frameworkTooltip;

    public AbstractFrameworkCard(String id, String img, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, -2, type, color, rarity, target);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(BaseMod.getKeywordTitle("m10robot:framework"));
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (frameworkTooltip == null)
        {
            frameworkTooltip = new ArrayList<>();
            frameworkTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("m10robot:framework"), BaseMod.getKeywordDescription("m10robot:framework")));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(frameworkTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    @Override
    public void applyPowers() {}

    @Override
    public void calculateCardDamage(AbstractMonster mo) {}

    @Override
    public void onRightClick() {}

    public void onEquip() {}

    public void onRemove() {}
}
