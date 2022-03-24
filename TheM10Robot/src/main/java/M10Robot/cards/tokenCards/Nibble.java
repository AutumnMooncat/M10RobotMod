package M10Robot.cards.tokenCards;

import M10Robot.M10RobotMod;
import M10Robot.actions.BitAttackAction;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.characters.M10Robot;
import M10Robot.orbs.BitOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import static M10Robot.M10RobotMod.makeCardPath;

public class Nibble extends AbstractDynamicCard implements TokenCard {

    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(Nibble.class.getSimpleName());
    public static final String IMG = makeCardPath("Nibble.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 0;
    private static final int EFFECT = 4;
    private static final int MONCH = 1;
    private static final int UPGRADE_PLUS_MONCH = 1;

    // /STAT DECLARATION/

    public Nibble() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = EFFECT;
        baseBlock = block = EFFECT;
        magicNumber = baseMagicNumber = MONCH;
        //CardModifierManager.addModifier(this, new AimedModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        for (AbstractOrb o : p.orbs) {
            if (o instanceof BitOrb) {
                for (int i = 0 ; i < magicNumber ; i++) {
                    this.addToBot(new BitAttackAction((BitOrb) o, p, m));
                }
            }
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.player.orbs.stream().anyMatch(o -> o instanceof BitOrb)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MONCH);
            initializeDescription();
        }
    }
}
