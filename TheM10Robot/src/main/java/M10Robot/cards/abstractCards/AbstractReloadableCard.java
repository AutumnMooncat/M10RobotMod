package M10Robot.cards.abstractCards;

import M10Robot.cardModifiers.ReloadingModifier;
import M10Robot.cards.interfaces.OnDiscardedCard;
import M10Robot.powers.interfaces.OnReloadPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractReloadableCard extends AbstractClickableCard implements OnDiscardedCard {
    public boolean needsReload;

    public AbstractReloadableCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
        CardModifierManager.addModifier(this, new ReloadingModifier());
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c == this) {
            useShot();
        }
    }

    @Override
    public void onDiscarded(boolean endOfTurn) {
        if(needsReload) {
            resetAmmo();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (isInAutoplay || !needsReload);
    }

    public void useShot() {
        needsReload = true;
        initializeDescription();
    }

    @Override
    public void onRightClick() {
        if (needsReload) {
            this.addToTop(new LoseEnergyAction(1));
            resetAmmo();
        }
    }

    public void resetAmmo() {
        playReloadSFX();
        needsReload = false;
        superFlash();
        initializeDescription();
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof OnReloadPower) {
                ((OnReloadPower) p).onReload(this);
            }
        }
    }

    public void playReloadSFX() {
        CardCrawlGame.sound.play("ATTACK_WHIFF_1", 0.1F);
        CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
    }
}
