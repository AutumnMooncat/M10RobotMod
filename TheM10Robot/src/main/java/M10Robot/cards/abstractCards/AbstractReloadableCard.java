package M10Robot.cards.abstractCards;

import M10Robot.M10RobotMod;
import M10Robot.cards.modules.AmmoBox;
import M10Robot.patches.CostBypassField;
import M10Robot.powers.ModulesPower;
import basemod.abstracts.CustomSavable;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.lang.reflect.Type;
import java.util.HashMap;

public abstract class AbstractReloadableCard extends AbstractClickableCard implements CustomSavable<HashMap<String, String>> {
    private static final String AMMO_AMOUNT = M10RobotMod.makeID("AMMO");
    private static final String DRAWN_ONCE = M10RobotMod.makeID("DRAWN");
    private static final String NEEDS_RELOAD = M10RobotMod.makeID("RELOAD");

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
            for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(this.uuid) && c instanceof AbstractReloadableCard) {
                    ((AbstractReloadableCard) c).drawnOnce = true;
                }
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (isInAutoplay || !needsReload || CostBypassField.bypassCost.get(this));
    }

    public void useShot() {
        boolean hasAmmoBox = false;
        for (AbstractCard c : ModulesPower.getEquippedModules()) {
            if (c instanceof AmmoBox) {
                hasAmmoBox = true;
                break;
            }
        }
        if (!hasAmmoBox || this.type != CardType.ATTACK) {
            if (ammoCount > 0) {
                ammoCount--;
            }
            isAmmoCountModified = ammoCount != baseAmmoCount;
            if (ammoCount == 0) {
                needsReload = true;
            }
            initializeDescription();
            for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(this.uuid) && c instanceof AbstractReloadableCard) {
                    ((AbstractReloadableCard) c).ammoCount = ammoCount;
                    ((AbstractReloadableCard) c).isAmmoCountModified = isAmmoCountModified;
                    ((AbstractReloadableCard) c).needsReload = needsReload;
                }
            }
        }
    }

    @Override
    public void onRightClick() {
        if (ammoCount < baseAmmoCount && EnergyPanel.totalCount > 0) {
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
        for (AbstractCard c :  AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(this.uuid) && c instanceof AbstractReloadableCard) {
                ((AbstractReloadableCard) c).ammoCount = ammoCount;
                ((AbstractReloadableCard) c).isAmmoCountModified = isAmmoCountModified;
                ((AbstractReloadableCard) c).needsReload = needsReload;
                ((AbstractReloadableCard) c).drawnOnce = drawnOnce;
            }
        }
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
        copy.ammoCount = this.ammoCount;
        copy.isAmmoCountModified = this.isAmmoCountModified;
        return copy;
    }

    @Override
    public HashMap<String, String> onSave() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AMMO_AMOUNT, Integer.toString(ammoCount));
        map.put(DRAWN_ONCE, Boolean.toString(drawnOnce));
        map.put(NEEDS_RELOAD, Boolean.toString(needsReload));
        return map;
    }

    @Override
    public void onLoad(HashMap<String, String> stringHashMap) {
        if (stringHashMap != null) {
            ammoCount = Integer.parseInt(stringHashMap.get(AMMO_AMOUNT));
            drawnOnce = Boolean.parseBoolean(stringHashMap.get(DRAWN_ONCE));
            needsReload = Boolean.parseBoolean(stringHashMap.get(NEEDS_RELOAD));
            isAmmoCountModified = ammoCount != baseAmmoCount;
            initializeDescription();
        }
    }

    @Override
    public Type savedType() {
        return new TypeToken<HashMap<String, String>>(){}.getType();
    }
}
