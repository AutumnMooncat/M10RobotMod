package M10Robot.cards;

import M10Robot.M10RobotMod;
import M10Robot.actions.SelfDestructAction;
import M10Robot.cardModifiers.HeavyModifier;
import M10Robot.cardModifiers.SpikyModifier;
import M10Robot.cards.abstractCards.AbstractDynamicCard;
import M10Robot.cards.interfaces.ModularDescription;
import M10Robot.cards.interfaces.NegativePrimaryEffect;
import M10Robot.characters.M10Robot;
import M10Robot.patches.RestorePositionPatches;
import M10Robot.powers.RecoilPower;
import M10Robot.vfx.BurnToAshEffect;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import java.util.ArrayList;

import static M10Robot.M10RobotMod.makeCardPath;

public class SelfDestruction extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = M10RobotMod.makeID(SelfDestruction.class.getSimpleName());
    public static final String IMG = makeCardPath("SelfDestruction2.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = M10Robot.Enums.GREEN_SPRING_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 10;

    // /STAT DECLARATION/

    public SelfDestruction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        isMultiDamage = true;
        CardModifierManager.addModifier(this, new HeavyModifier());
        CardModifierManager.addModifier(this, new SpikyModifier());
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.setBackUp(p, p.drawX, p.drawY, p.hb.cX, p.hb.cY);
                this.isDone = true;
            }
        });
        this.addToBot(new SelfDestructAction(p, m, multiDamage, damageTypeForTurn, 0, false));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                RestorePositionPatches.removeBackUp(p);
                this.isDone = true;
            }
        });
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
