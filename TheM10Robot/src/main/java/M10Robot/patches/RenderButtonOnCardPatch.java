package M10Robot.patches;

import M10Robot.M10RobotMod;
import M10Robot.cards.abstractCards.AbstractBoosterCard;
import basemod.ClickableUIElement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Random;

public class RenderButtonOnCardPatch {

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class ClickableElementField {
        public static SpireField<ClickableCardElement> element = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
    public static class TrulyForConfirmationField {
        public static SpireField<Boolean> justForConfirmation = new SpireField<>(() -> Boolean.FALSE);
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class DoNotGiveCardsPls {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> stop(GridCardSelectScreen __instance) {
            if (TrulyForConfirmationField.justForConfirmation.get(__instance) && __instance.isJustForConfirming) {
                AbstractDungeon.closeCurrentScreen();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "iterator");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class StopPickingUpTheDamnCard {
        @SpirePrefixPatch
        public static SpireReturn<?> stop(AbstractPlayer __instance) {
            if (__instance.hoveredCard != null) {
                ClickableCardElement e = ClickableElementField.element.get(__instance.hoveredCard);
                if (e != null) {
                    if (InputHelper.justClickedLeft && e.getHitbox().hovered) {
                        AbstractDungeon.player.releaseCard();
                        e.onManualClick();
                        e.update();
                        return SpireReturn.Return(false);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "update")
    public static class UpdateClickableElementPatch
    {
        @SpirePrefixPatch
        public static void UpdateClickableElement(AbstractCard __instance) {
            ClickableCardElement e = ClickableElementField.element.get(__instance);
            if (e != null) {
                if (InputHelper.justClickedLeft && e.getHitbox().hovered) {
                    AbstractDungeon.player.releaseCard();
                    e.onManualClick();
                }
                e.update();
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderOnCardPatch
    {
        @SpirePostfixPatch
        public static void RenderOnCard(AbstractCard __instance, SpriteBatch sb) {
            if (AbstractDungeon.player != null && validLocation(__instance)) {
                if (BoosterFieldPatch.hasBoosterEquipped(__instance)) {
                    if (ClickableElementField.element.get(__instance) == null) {
                        ClickableElementField.element.set(__instance,
                                new ClickableCardElement(
                                        __instance,
                                        new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("128/" + "artifact")));
                    } else {
                        renderHelper(sb, __instance.current_x, __instance.current_y, __instance);
                    }
                }
            }
        }

        //Don't bother rendering if it isn't in one of 4 immediately viewable locations. We also don't want to render in master deck
        private static boolean validLocation(AbstractCard c) {
            return AbstractDungeon.player.hand.contains(c) ||
                    AbstractDungeon.player.drawPile.contains(c) ||
                    AbstractDungeon.player.discardPile.contains(c) ||
                    AbstractDungeon.player.exhaustPile.contains(c);
        }


        private static void renderHelper(SpriteBatch sb, float drawX, float drawY, AbstractCard C) {
            ClickableCardElement e = ClickableElementField.element.get(C);
            AtlasRegion img = e.imageToRender;
            //sb.setColor(Color.WHITE);

            //Used to offset where the image will appear on the card, relative to the centre of the card
            //float dx = 155f;
            //float dy = 210f;
            //float dx = -(AbstractCard.RAW_W * Settings.scale) / 2f + img.originalWidth / 2.0F;
            //float dy = 0;//(AbstractCard.RAW_H * Settings.scale) / 2f;
            float dx = 0;
            float dy = img.originalWidth / 2.0F;//(AbstractCard.RAW_H * Settings.scale) / 5f;

            //Constants pulled from sb.draw, modified by dx and dy. This dumps the image directly in the centre of the card when dy and dy are 0
            float x = drawX + dx + img.offsetX - (float) img.originalWidth / 2.0F;
            float y = drawY + dy + img.offsetY - (float) img.originalHeight / 2.0F;
            float originX = img.originalWidth / 2.0F - img.offsetX - dx;
            float originY = img.originalHeight / 2.0F - img.offsetY - dy;
            float worldOriginX = x + originX;
            float worldOriginY = y + originY;
            float width = img.packedWidth;
            float height = img.packedHeight;
            float scaleX = C.drawScale * Settings.scale;
            float scaleY = C.drawScale * Settings.scale;
            float rotation = C.angle;
            //sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
            sb.draw(img, x, y, originX, originY, width, height, scaleX, scaleY, rotation);


            //Math stuff to determine the correct place to move the hitbox after factoring in scale and rotation from sb.draw
            float fx = -originX;
            float fy = -originY;
            if (scaleX != 1.0F || scaleY != 1.0F) {
                fx *= scaleX;
                fy *= scaleY;
            }

            float x1;
            float y1;
            float u;
            float v;
            if (rotation != 0.0F) {
                u = MathUtils.cosDeg(rotation);
                v = MathUtils.sinDeg(rotation);
                x1 = u * fx - v * fy;
                y1 = v * fx + u * fy;
            } else {
                x1 = fx;
                y1 = fy;
            }
            x1 += worldOriginX;
            y1 += worldOriginY;

            //Move scale and angle the hitbox, then render it
            e.setScale(C.drawScale * Settings.scale);
            e.move(x1, y1);
            //e.setAngle(C.angle);
            e.render(sb);
        }
    }

    public static class ClickableCardElement extends ClickableUIElement {
        String[] TEXT = CardCrawlGame.languagePack.getUIString(M10RobotMod.makeID("Boosters")).TEXT;
        private static final float hitboxSize = 80f;
        private static final float moveDelta = hitboxSize/2;
        private boolean wasClicked = false;
        private boolean canOpen = true;
        private final AbstractCard card;
        public final TextureAtlas.AtlasRegion imageToRender;
        private float scale = 1f * Settings.scale;
        private CardGroup backupGroup;
        private AbstractDungeon.CurrentScreen backupScreen;
        private static final float INTERVAL = 0.1f;
        private float particleTimer;
        private static Random rng = new Random();

        public ClickableCardElement(AbstractCard card, TextureAtlas.AtlasRegion imageToRender) {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""), 0, 0, hitboxSize, hitboxSize);
            this.card = card;
            this.imageToRender = imageToRender;
        }

        public void move(float x, float y) {
            move(x, y, 0);
        }

        private void move(float x, float y, float d) {
            this.setX(x-(d * Settings.scale));
            this.setY(y-(d * Settings.scale));
        }

        public void setScale(float newScale) {
            if (newScale != scale) {
                this.hb_w = hb_w * newScale / this.scale;
                this.hb_h = hb_h * newScale / this.scale;
                this.hitbox.resize(hb_w, hb_h);
                this.scale = newScale;
            }
        }

        public Hitbox getHitbox() {
            return hitbox;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        @Override
        protected void onHover() {
            this.particleTimer -= Gdx.graphics.getRawDeltaTime();
            if (this.particleTimer < 0.0F) {
                float xOff = ((hitbox.width) * (float) rng.nextGaussian())*0.25f;
                if(MathUtils.randomBoolean()) {
                    xOff = -xOff;
                }
                float yOff = ((hitbox.height) * (float) rng.nextGaussian())*0.25f;
                if(MathUtils.randomBoolean()) {
                    yOff = -xOff;
                }
                //AbstractDungeon.effectList.add(new StraightFireParticle(owner.drawX + xOff, owner.drawY + MathUtils.random(owner.hb_h/2f), 75f));
                AbstractDungeon.topLevelEffects.add(new UncommonPotionParticleEffect(x+hitbox.width/2+xOff, y+hitbox.height/2+yOff));
                this.particleTimer = INTERVAL;
            }
        }

        @Override
        protected void onUnhover() {}

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                //wasClicked = true;
            }
        }

        public void onManualClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                wasClicked = true;
            }
        }

        private boolean canOverrideScreen() {
            return !AbstractDungeon.isScreenUp
                    || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DISCARD_VIEW
                    || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.EXHAUST_VIEW
                    || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GAME_DECK_VIEW;
        }

        @Override
        public void update() {
            super.update();
            if (wasClicked && canOpen && canOverrideScreen()) {
                if (AbstractDungeon.isScreenUp) {
                    //backupScreen = AbstractDungeon.screen;
                    //backupGroup = AbstractDungeon.gridSelectScreen.targetGroup;
                    AbstractDungeon.closeCurrentScreen();
                    /*AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            //AbstractDungeon.closeCurrentScreen();
                            //AbstractDungeon.gridSelectScreen.targetGroup = backupGroup;
                            //AbstractDungeon.isScreenUp = true;
                            //ReflectionHacks.RMethod m = ReflectionHacks.privateMethod(AbstractDungeon.class, "openPreviousScreen", AbstractDungeon.CurrentScreen.class);
                            //m.invoke(CardCrawlGame.dungeon, backupScreen);
                            //AbstractDungeon.openPreviousScreen(backupScreen);
                            this.isDone = true;
                        }
                    });*/
                }
                for (AbstractCard c : BoosterFieldPatch.getEquippedBoosters(card).group) {
                    c.applyPowers();
                    c.initializeDescription();
                }
                canOpen = false;
                if (AbstractDungeon.player.hand.contains(card)) {
                    AbstractDungeon.gridSelectScreen.open(BoosterFieldPatch.getEquippedBoosters(card), 0, true, TEXT[2]);
                } else {
                    TrulyForConfirmationField.justForConfirmation.set(AbstractDungeon.gridSelectScreen, true);
                    AbstractDungeon.gridSelectScreen.openConfirmationGrid(BoosterFieldPatch.getEquippedBoosters(card), TEXT[3]);
                }
                AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        TrulyForConfirmationField.justForConfirmation.set(AbstractDungeon.gridSelectScreen, false);
                        //CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                            if (c instanceof AbstractBoosterCard) {
                                BoosterFieldPatch.unequipBooster(card, (AbstractBoosterCard) c);
                            }
                            //Moved to unequipBooster
                            //temp.addToTop(c);
                            //temp.moveToDiscardPile(c);
                        }
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        wasClicked = false;
                        canOpen = true;
                        this.isDone = true;
                    }
                });
            }
        }
    }

}
