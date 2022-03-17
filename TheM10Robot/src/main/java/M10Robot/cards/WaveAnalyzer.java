package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.BuffCardAction;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import M10Robot.cards.uniqueCards.UniqueCard;
import M10Robot.characters.M10Robot;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static M10Robot.M10RobotMod.makeCardPath;

public class WaveAnalyzer extends AbstractSwappableCard implements UniqueCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(WaveAnalyzer.class.getSimpleName());
    public static final String IMG = makeCardPath("WaveAnalyzer.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int BOOST = 2;

    // /STAT DECLARATION/

    public WaveAnalyzer() {
        this(null);
    }

    public WaveAnalyzer(AbstractSwappableCard linkedCard) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = BOOST;
        if (linkedCard == null) {
            setLinkedCard(new WaveGenerator(this));
        } else {
            setLinkedCard(linkedCard);
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP"));
        for (AbstractMonster aM: AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new VFXAction(p, new ShockWaveEffect(aM.hb.cX, aM.hb.cY, Settings.GOLD_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.1F));
            }
        }
        this.addToBot(new GainBlockAction(p, p, block));
        this.addToBot(new BuffCardAction(cardsToPreview, BuffCardAction.BUFF_TYPE.DAMAGE, magicNumber));
        /*for (AbstractMonster aM: AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                this.addToBot(new GainBlockAction(p, p, block));
            }
        }*/
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
            super.upgrade();
        }
    }
}
