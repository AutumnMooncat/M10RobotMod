package M10Robot.patches;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.evacipated.cardcrawl.mod.stslib.patches.cardInterfaces.BranchingUpgradesPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

public class BranchingFixPatches {

    @SpirePatch(clz = BranchingUpgradesPatch.ConfirmUpgrade.class, method = "Insert")
    public static class randomUpgradeFix {
        public static void Prefix(GridCardSelectScreen __instance) {
            AbstractCard hoveredCard = BranchingUpgradesPatch.getHoveredCard();
            if (hoveredCard instanceof BranchingUpgradesCard && !BranchingUpgradesPatch.BranchSelectFields.isBranchUpgrading.get(__instance)) {
                ((BranchingUpgradesCard)hoveredCard).setUpgradeType(BranchingUpgradesCard.UpgradeType.NORMAL_UPGRADE);
                BranchingUpgradesPatch.BranchSelectFields.isBranchUpgrading.set(__instance, false);
            }
        }
    }
}
