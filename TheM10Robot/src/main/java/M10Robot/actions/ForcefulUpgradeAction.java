package M10Robot.actions;

import M10Robot.patches.ForcedUpgradeField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.HashMap;

public class ForcefulUpgradeAction extends AbstractGameAction {
    private final boolean allCards;

    public ForcefulUpgradeAction(boolean allCards) {
        this.allCards = allCards;
    }

    @Override
    public void update() {
        //Map to store if cards were originally upgraded or not
        HashMap<AbstractCard, Boolean> upgradedMap = new HashMap<>();
        //Map to store if the card was allowed to upgrade or not
        HashMap<AbstractCard, Boolean> canUpgradeMap = new HashMap<>();
        //Loop through all cards first
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            //Grab the old upgraded variable
            upgradedMap.put(c, c.upgraded);
            //Grab if the card was allowed to upgrade
            canUpgradeMap.put(c, c.canUpgrade());
            //Set the card as not upgraded, since that's the check Armaments Action makes
            c.upgraded = false;
        }
        //Add this to top, since we add the Armaments Action to top, this comes second
        this.addToTop(new RectifyAction(upgradedMap, canUpgradeMap));
        this.addToTop(new ArmamentsAction(allCards));
        this.isDone = true;
    }

    private static class RectifyAction extends AbstractGameAction {
        private final HashMap<AbstractCard, Boolean> upgradedMap;
        private final HashMap<AbstractCard, Boolean> canUpgradeMap;

        public RectifyAction(HashMap<AbstractCard, Boolean> upgradedMap, HashMap<AbstractCard, Boolean> canUpgradeMap) {
            this.upgradedMap = upgradedMap;
            this.canUpgradeMap = canUpgradeMap;
        }

        @Override
        public void update() {
            //Loop through all cards again
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                //If the card is in our map (it's possible new cards were added)
                if (upgradedMap.containsKey(c)) {
                    //If the card isnt currently upgraded, that means we didnt choose it (since all cards were set as not upgraded)
                    if (!c.upgraded) {
                        //Restore its old upgraded boolean
                        c.upgraded = upgradedMap.get(c);
                    } else { //This is the card that we just upgraded, as its the only one possible to have the upgrade boolean true
                        //Check to see if it was originally allowed to upgrade or not (some cards can upgrade more than one time)
                        if (!canUpgradeMap.get(c)) {
                            //If it wasn't allowed to upgrade before, we forced an upgrade. Save this is a field for use in making copies
                            ForcedUpgradeField.wasForced.set(c, true);
                        }
                    }
                }
            }
            this.isDone = true;
        }
    }
}
