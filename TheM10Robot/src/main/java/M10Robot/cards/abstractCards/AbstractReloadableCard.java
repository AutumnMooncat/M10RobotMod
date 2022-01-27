package M10Robot.cards.abstractCards;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.ReloadingModifier;
import M10Robot.cards.interfaces.OnDiscardedCard;
import M10Robot.powers.interfaces.OnReloadPower;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class AbstractReloadableCard extends AbstractClickableCard implements OnDiscardedCard {
    private static final UIStrings strings = CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Reloadable"));
    public static final Color RELOADING_RED = Color.RED.cpy();
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

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (needsReload && !isInAutoplay) {
            canUse = false;
            this.cantUseMessage = strings.TEXT[0];
        }
        return canUse;
    }

    public void useShot() {
        needsReload = true;
        initializeDescription();
    }

    @Override
    public void onRightClick() {
        if (needsReload && EnergyPanel.getCurrentEnergy() > 0) {
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
