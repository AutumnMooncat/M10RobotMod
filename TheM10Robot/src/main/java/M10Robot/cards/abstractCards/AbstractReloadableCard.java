package M10Robot.cards.abstractCards;

import M10Robot.cards.modules.AmmoBox;
import M10Robot.powers.ModulesPower;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public abstract class AbstractReloadableCard extends AbstractClickableCard {

    public boolean needsReload;
    public boolean drawnOnce;

    public AbstractReloadableCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (c == this) {
            useShot();
        }
    }

    @Override
    public void applyPowers() {
        int boost = 0;
        for (AbstractCard c : ModulesPower.getEquippedModules()) {
            if (c instanceof AmmoBox) {
                boost += c.magicNumber;
            }
        }
        baseDamage += boost;
        super.applyPowers();
        baseDamage -= boost;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int boost = 0;
        for (AbstractCard c : ModulesPower.getEquippedModules()) {
            if (c instanceof AmmoBox) {
                boost += c.magicNumber;
            }
        }
        baseDamage += boost;
        super.calculateCardDamage(mo);
        baseDamage -= boost;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void triggerWhenDrawn() {
        for (AbstractCard c : ModulesPower.getEquippedModules()) {
            if (c instanceof AmmoBox) {
                resetAmmo();
                break;
            }
        }
        if (drawnOnce) {
            resetAmmo();
        } else if (needsReload) {
            drawnOnce = true;
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && !needsReload;
    }

    public void useShot() {
        boolean hasAmmoBox = false;
        for (AbstractCard c : ModulesPower.getEquippedModules()) {
            if (c instanceof AmmoBox) {
                hasAmmoBox = true;
                break;
            }
        }
        if (!hasAmmoBox) {
            if (ammoCount > 0) {
                ammoCount--;
            }
            isAmmoCountModified = ammoCount != baseAmmoCount;
            if (ammoCount == 0) {
                needsReload = true;
            }
            initializeDescription();
        }
    }

    @Override
    public void onRightClick() {
        if (needsReload && EnergyPanel.totalCount > 0) {
            this.addToTop(new LoseEnergyAction(1));
            resetAmmo();
        }
    }

    public void resetAmmo() {
        playReloadSFX();
        ammoCount = baseAmmoCount;
        isAmmoCountModified = false;
        needsReload = false;
        drawnOnce = false;
        superFlash();
        initializeDescription();
    }

    public void playReloadSFX() {
        CardCrawlGame.sound.play("ATTACK_WHIFF_1", 0.1F);
        CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractReloadableCard copy = (AbstractReloadableCard) super.makeStatEquivalentCopy();
        copy.needsReload = this.needsReload;
        copy.drawnOnce = this.drawnOnce;
        return copy;
    }
}
