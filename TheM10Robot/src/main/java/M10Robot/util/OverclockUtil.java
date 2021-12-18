package M10Robot.util;

import M10Robot.cardModifiers.OverclockModifier;
import M10Robot.cards.interfaces.CannotOverclock;
import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.powers.ComponentsPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OverclockUtil {
    public static final int BASE_COST = 1;
//    public static final int COST_PER_OVERCLOCK = 0;

    public static int getOverclockCost(AbstractCard c) {
//        if (CardModifierManager.hasModifier(c, OverclockModifier.ID)) {
//            return BASE_COST + ((OverclockModifier)CardModifierManager.getModifiers(c, OverclockModifier.ID).get(0)).getOverclocks() * COST_PER_OVERCLOCK;
//        }
        return BASE_COST;
    }

    public static boolean hasOverclock(AbstractCard c) {
        return !CardModifierManager.getModifiers(c, OverclockModifier.ID).isEmpty();
    }

    public static int getOverClockPercent(AbstractCard c) {
        return ((OverclockModifier) CardModifierManager.getModifiers(c, OverclockModifier.ID).get(0)).getRawPercent();
    }

    public static int getOverclockCost(AbstractCustomOrb o) {
        return BASE_COST /*+ o.timesUpgraded * COST_PER_OVERCLOCK*/;
    }

    private static boolean hasComponentsPower() {
        return AbstractDungeon.player.hasPower(ComponentsPower.POWER_ID);
    }

    private static int getComponentsAmount() {
        if (hasComponentsPower()) {
            return AbstractDungeon.player.getPower(ComponentsPower.POWER_ID).amount;
        } else {
            return 0;
        }
    }

    public static boolean canOverclock(AbstractCard c) {
        return !(c instanceof CannotOverclock) && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE;
    }

    public static boolean canOverclock(AbstractCustomOrb o) {
        return getComponentsAmount() >= getOverclockCost(o) && o.canUpgrade();
    }

    public static void spendComponents(int amount) {
        if (hasComponentsPower()) {
            AbstractPower p = AbstractDungeon.player.getPower(ComponentsPower.POWER_ID);
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, p, amount));
        }
    }

    public static String getOverclockPrefix(AbstractCard c) {
        return "";
    }

    public static String getOverclockSuffix(AbstractCard c) {
        if (hasOverclock(c)) {
            return " +"+getOverClockPercent(c)+"% ";
        }
        return "";
    }
}
