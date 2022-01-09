package M10Robot.cutStuff.powers;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractSwappableCard;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class RelayPower extends AbstractPower implements CloneablePowerInterface, NonStackablePower {

    public static final String POWER_ID = M10RobotMod.makeID("RelayPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public final CardGroup relayCards;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public RelayPower(AbstractCreature owner, List<AbstractCard> relayCards) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

        this.relayCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : relayCards) {
            this.relayCards.addToTop(c);
        }

        this.amount = this.relayCards.size();

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("rushdown");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public RelayPower(AbstractCreature owner, CardGroup relayCards) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false;

        if (relayCards != null) {
            this.relayCards = new CardGroup(relayCards, CardGroup.CardGroupType.UNSPECIFIED);
        } else {
            this.relayCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        }

        this.amount = this.relayCards.size();

        // We load those txtures here.
        //this.loadRegion("cExplosion");
        this.loadRegion("rushdown");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        for (AbstractCard card : relayCards.group) {
            //relayCards.moveToHand(card);
            card.unfadeOut();
            card.unhover();
            if (AbstractDungeon.player.drawPile.contains(card)) {
                AbstractDungeon.player.drawPile.moveToHand(card);
            }
            if (AbstractDungeon.player.discardPile.contains(card)) {
                AbstractDungeon.player.discardPile.moveToHand(card);
            }
            if (card instanceof AbstractSwappableCard) {
                card.cardsToPreview.unfadeOut();
                card.cardsToPreview.unhover();
                if (AbstractDungeon.player.drawPile.contains(card.cardsToPreview)) {
                    AbstractDungeon.player.drawPile.moveToHand(card.cardsToPreview);
                }
                if (AbstractDungeon.player.discardPile.contains(card.cardsToPreview)) {
                    AbstractDungeon.player.discardPile.moveToHand(card.cardsToPreview);
                }
            }
        }
        relayCards.clear();
        updateDescription();
        this.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]).append(" NL ");
        for (int i = 0 ; i < relayCards.group.size() ; i++) {
            sb.append(relayCards.group.get(i).name);
            if (relayCards.group.size() - 1 > i) {
                sb.append(", ");
            } else {
                sb.append(".");
            }

        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new RelayPower(owner, new CardGroup(relayCards, CardGroup.CardGroupType.UNSPECIFIED));
    }
}
