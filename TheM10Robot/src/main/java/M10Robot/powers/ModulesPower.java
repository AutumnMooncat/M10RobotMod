package M10Robot.powers;

import M10Robot.M10RobotMod;
import M10Robot.actions.EquipBoosterAction;
import M10Robot.actions.EvokeSpecificOrbMultipleTimesAction;
import M10Robot.cardModifiers.DealDamageEffect;
import M10Robot.cards.abstractCards.AbstractModuleCard;
import M10Robot.cards.modules.*;
import M10Robot.cards.tempCards.WeaponPolishBooster;
import basemod.ClickableUIElement;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;

public class ModulesPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = M10RobotMod.makeID("ModulesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ClickablePowerElement clickableElement;
    public final CardGroup modules;
    public int evokedThisTurn;
    private final ArrayList<AbstractOrb> orbsEvokedThisTurn = new ArrayList<>();
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ModulesPower(AbstractCreature owner, int amount) {
        this(owner, amount, null);
    }

    public ModulesPower(AbstractCreature owner, int amount, CardGroup modules) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

        this.priority = -1;

        if (modules != null) {
            this.modules = modules;
        } else {
            this.modules = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        }

        clickableElement = new ClickablePowerElement();
        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("heatsink");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != owner && info.type == DamageInfo.DamageType.NORMAL) {
            int str = 0;
            for (AbstractCard m : modules.group) {
                if (m instanceof Rumble) {
                    str += m.magicNumber;
                }
            }
            if (str > 0) {
                this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, str), str, true));
                this.addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, str), str, true));
                flash();
            }
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard.type == AbstractCard.CardType.ATTACK) {
            int increase = 0;
            for (AbstractCard m : modules.group) {
                if (m instanceof WeaponPolish) {
                    increase += m.magicNumber;
                }
            }
            if (increase > 0) {
                WeaponPolishBooster booster = new WeaponPolishBooster(increase);
                this.addToTop(new EquipBoosterAction(usedCard, booster, true));
                flash();
            }
        }
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        boolean flash = false;
        int evokes = 0;
        for (AbstractCard m : modules.group) {
            if (m instanceof AutoCaster) {
                evokes += m.magicNumber;
            }
        }
        if (evokes > 0) {
            this.addToTop(new EvokeSpecificOrbMultipleTimesAction(orb, evokes));
            flash = true;
        }
        if (flash) {
            flash();
        }
        super.onChannel(orb);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {
        int doublers = 0;
        for (AbstractCard m : modules.group) {
            if (m instanceof DoubleCaster) {
                doublers += m.magicNumber;
            }
        }
        if (evokedThisTurn < doublers && !orbsEvokedThisTurn.contains(orb)) {
            orbsEvokedThisTurn.add(orb);
            evokedThisTurn++;
            orb.onEvoke();
            flash();
        }
        super.onEvokeOrb(orb);
    }

    public void atStartOfTurn() {
        boolean flash = false;
        int energy = 0;
        int focus = 0;
        int lockon = 0;
        for (AbstractCard m : modules.group) {
            if (m instanceof PowerSavings) {
                energy += m.magicNumber;
            }
            if (m instanceof Concentration) {
                if (((AbstractModuleCard)m).secondMagicNumber < m.magicNumber) {
                    ((AbstractModuleCard)m).secondMagicNumber++;
                    focus++;
                }
            }
            if (m instanceof Repeater) {
                if (CardModifierManager.modifiers(m).size() > 0) {
                    CardModifierManager.onUseCard(m, AbstractDungeon.getRandomMonster(), null);
                    flash = true;
                }
            }
            if (m instanceof TargetingSystem) {
                lockon += m.magicNumber;
            }
        }
        if (energy > 0) {
            this.addToBot(new GainEnergyAction(energy));
            flash = true;
        }
        if (focus > 0) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, focus)));
            flash = true;
        }
        if (lockon > 0) {
            //this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RepairableArmorPower(AbstractDungeon.player, protect)));
            for (int i = 0 ; i < lockon ; i++) {
                AbstractMonster aM = AbstractDungeon.getRandomMonster();
                this.addToBot(new ApplyPowerAction(aM, owner, new LockOnPower(aM, 1), 1, true));
            }
            flash = true;
        }
        if (flash) {
            this.flash();
        }
        evokedThisTurn = 0;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        boolean flash = false;
        for (AbstractCard m : modules.group) {
            if (m instanceof WeaponPlatform) {
                for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                    if (!mon.isDeadOrEscaped() && mon.hasPower(LockOnPower.POWER_ID)) {
                        this.addToBot(new DamageAction(mon, new DamageInfo(owner, m.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
                    }
                }
                flash = true;
            }
        }
        if (flash) {
            this.flash();
        }
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        for (AbstractCard m : modules.group) {
            if (m instanceof PowerSavings) {
                damage *= (1 - (m.magicNumber/100f));
            }
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]).append(DESCRIPTIONS[1]).append(amount).append(" NL ");
        for (int i = 0 ; i < amount ; i++) {
            sb.append(DESCRIPTIONS[3]).append(i+1).append(": ");
            if (modules.group.size() > i) {
                sb.append(modules.group.get(i).name);
            } else {
                sb.append(DESCRIPTIONS[2]);
            }
            sb.append(" NL ");
        }
        sb.append(DESCRIPTIONS[4]);
        description = sb.toString();
    }

    public static int getModuleAmount(Class<AbstractCard> moduleClass) {
        int ret = 0;
        for (AbstractCard mod : getEquippedModules()) {
            if (moduleClass.isInstance(mod)) {
                ret++;
            }
        }
        return ret;
    }


    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.modules.size()), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ModulesPower(owner, amount, new CardGroup(modules, CardGroup.CardGroupType.UNSPECIFIED));
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        clickableElement.move(x, y);
        clickableElement.render(sb);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        clickableElement.update();
    }

    public boolean canEquip() {
        return modules.size() < amount;
    }

    public void equipModule(AbstractModuleCard card) {
        card.onEquip();
        modules.addToTop(card);
        AbstractDungeon.player.onCardDrawOrDiscard();
        updateDescription();
    }

    public static String getCantEquipMessage() {
        return DESCRIPTIONS[6];
    }

    public static ArrayList<AbstractCard> getEquippedModules() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(ModulesPower.POWER_ID)) {
            return ((ModulesPower)AbstractDungeon.player.getPower(ModulesPower.POWER_ID)).modules.group;
        }
        return new ArrayList<>();
    }

    public class ClickablePowerElement extends ClickableUIElement {
        private static final float hitboxSize = 40f;
        private static final float moveDelta = hitboxSize/2;
        private boolean wasClicked = false;
        private boolean needsToOpen = true;

        public ClickablePowerElement() {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""),//carddraw
                    0, 0,
                    hitboxSize, hitboxSize);
        }

        public void move(float x, float y) {
            move(x, y, moveDelta);
        }

        public void move(float x, float y, float d) {
            this.setX(x-(d * Settings.scale));
            this.setY(y-(d * Settings.scale));
        }


        @Override
        protected void onHover() {}

        @Override
        protected void onUnhover() {}

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                wasClicked = true;
            }
        }

        @Override
        public void update() {
            super.update();
            if (wasClicked) {
                if (needsToOpen) {
                    for (AbstractCard c : modules.group) {
                        c.applyPowers();
                        c.initializeDescription();
                    }
                    needsToOpen = false;
                    AbstractDungeon.gridSelectScreen.open(modules, 0, true, DESCRIPTIONS[5]);
                } else {
                    for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                        modules.moveToDiscardPile(c);
                        if (c instanceof AbstractModuleCard) ((AbstractModuleCard) c).onRemove();
                        //modules.removeCard(c);
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    wasClicked = false;
                    needsToOpen = true;
                    updateDescription();
                }
            }
        }
    }
}
