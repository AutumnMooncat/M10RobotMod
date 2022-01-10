package M10Robot.potions;

import M10Robot.M10RobotMod;
import M10Robot.powers.EMPPower;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class LockingPotion extends CustomPotion {


    public static final String POTION_ID = M10RobotMod.makeID("LockingPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 3;

    public LockingPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.H, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            this.addToBot(new ApplyPowerAction(aM, AbstractDungeon.player, new EMPPower(aM, potency)));
        }
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return EFFECT;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle("m10robot:stasis"),
                BaseMod.getKeywordDescription("m10robot:stasis")
        ));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LockingPotion();
    }
}