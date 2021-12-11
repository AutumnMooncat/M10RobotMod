package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cardModifiers.AimedModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.characters.M10Robot;
import M10Robot.vfx.BurnToAshEffect;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.XCostModifier;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.InversionBeamEffect;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class AirRaid extends AbstractDynamicCard implements ModularDescription {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(AirRaid.class.getSimpleName());
    public static final String IMG = makeCardPath("AirRaid.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = -1;
    private static final int DAMAGE = 8;
    private static final int BONUS_HITS = 0;
    private static final int UPGRADE_PLUS_BONUS_HITS = 1;

    // /STAT DECLARATION/


    //TODO buff or rework
    public AirRaid() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_HITS;
        isMultiDamage = true;
        CardModifierManager.addModifier(this, new AimedModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;

        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += ChemicalX.BOOST;
            p.getRelic("Chemical X").flash();
        }

        ArrayList<List<?>> lists = new ArrayList<>();
        lists.add(AbstractDungeon.player.hand.group);
        lists.add(AbstractDungeon.player.drawPile.group);
        lists.add(AbstractDungeon.player.discardPile.group);
        lists.add(AbstractDungeon.player.powers);
        lists.add(AbstractDungeon.player.relics);
        lists.add(CardModifierPatches.CardModifierFields.cardModifiers.get(this));
        for (List<?> list : lists) {
            for (Object item : list) {
                if (item instanceof XCostModifier) {
                    XCostModifier mod = (XCostModifier)item;
                    if (mod.xCostModifierActive(this)) {
                        effect += mod.modifyX(this);
                    }
                }
            }
        }

        effect += magicNumber;

        for (int i = 0 ; i < effect ; i++) {
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped() /*&& aM.hasPower(LockOnPower.POWER_ID)*/) {
                    this.addToBot(new VFXAction(new InversionBeamEffect(aM.hb.cX), 0.1f));
                    this.addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(aM.hb.cX, aM.hb.cY));
                            AbstractDungeon.effectsQueue.add(new BurnToAshEffect(aM.hb.cX, aM.hb.cY));
                            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, false);
                            this.isDone = true;
                        }
                    });
                    this.addToBot(new SFXAction("WATCHER_HEART_PUNCH"));
                    this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, true));
                }
            }
        }

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_HITS);
            initializeDescription();
        }
    }

    @Override
    public void changeDescription() {
        if (DESCRIPTION != null) {
            if (magicNumber > 0) {
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = DESCRIPTION;
            }
        }
    }
}
