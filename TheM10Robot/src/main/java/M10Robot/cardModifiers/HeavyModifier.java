package M10Robot.cardModifiers;

import M10Robot.M10RobotMod;
import M10Robot.powers.RecoilPower;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public class HeavyModifier extends AbstractCardModifier {
    public static final String ID = M10RobotMod.makeID("HeavyModifier");
    public static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] TEXT = STRINGS.EXTENDED_DESCRIPTION;

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RecoilPower(AbstractDungeon.player, 1)));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        rawDescription = TEXT[0] + rawDescription;
        return rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HeavyModifier();
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
