package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

import java.util.ArrayList;
import java.util.List;

import static M10Robot.M10RobotMod.makeCardPath;

public class HeatSeekers extends AbstractSwappableCard implements UniqueCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(HeatSeekers.class.getSimpleName());
    public static final String IMG = makeCardPath("HeatSeekers.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    protected static ArrayList<TooltipInfo> toolTips;

    // /STAT DECLARATION/

    public HeatSeekers() {
        this(null);
    }

    public HeatSeekers(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        this.isMultiDamage = true;
        if (linkedCard == null) {
            setLinkedCard(new ThermalImaging(this));
        } else {
            setLinkedCard(linkedCard);
        }
        //CardModifierManager.addModifier(this, new AimedModifier());
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (toolTips == null) {
            toolTips = new ArrayList<>();
            toolTips.add(new TooltipInfo(TipHelper.capitalize(GameDictionary.CHANNEL.NAMES[0]), GameDictionary.keywords.get(GameDictionary.CHANNEL.NAMES[0])));
            toolTips.add(new TooltipInfo(BaseMod.getKeywordTitle("m10robot:bit"), BaseMod.getKeywordDescription("m10robot:bit")));
        }
        return toolTips;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster aM: AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped() && aM.hasPower(LockOnPower.POWER_ID)) {
                this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
            }
        }
        //this.addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        //this.addToBot(new BuffCardAction(this, BuffCardAction.BUFF_TYPE.DAMAGE, magicNumber));
    }

    public void triggerOnGlowCheck() {
        this.glowColor = Color.RED.cpy();
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.hasPower(LockOnPower.POWER_ID))) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
            super.upgrade();
        }
    }
}
