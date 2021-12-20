//package M10Robot.tutorials;
//
//import M10Robot.M10RobotMod;
//import M10Robot.cards.Defend;
//import M10Robot.cutStuff.boosters.PolishingKit;
//import M10Robot.cutStuff.boosters.ScrapBooster;
//import M10Robot.cutStuff.boosters.SubroutineDraw;
//import M10Robot.cutStuff.TargetingSystem;
//import M10Robot.characters.M10Robot;
//import M10Robot.patches.BoosterFieldPatch;
//import basemod.ReflectionHacks;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.cards.AbstractCard;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
//import com.megacrit.cardcrawl.localization.TutorialStrings;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.powers.HeatsinkPower;
//import com.megacrit.cardcrawl.ui.FtueTip;
//import com.megacrit.cardcrawl.ui.buttons.GotItButton;
//import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
//
//import java.io.IOException;
//
//public class BoosterTutorial extends FtueTip {
//    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(M10RobotMod.makeID("BoosterTutorial"));
//    public static final String[] MSG = tutorialStrings.TEXT;
//    public static final String[] LABEL = tutorialStrings.LABEL;
//    private int currentSlot = 0;
//    private int maxSlots = 4;
//    private static final String title1 = LABEL[0];
//    //private static final String title2 = LABEL[1];
//    private static final AbstractCard card1 = new PolishingKit();
//    private static final AbstractCard card2 = new Defend();
//    private static final AbstractCard card3 = new TargetingSystem();
//    private static final AbstractPower pow = new HeatsinkPower(null, 0);
//    private static final AtlasRegion img = pow.region128;
//    private final GotItButton button;
//
//    public BoosterTutorial() {
//        super(title1, MSG[0], Settings.WIDTH / 2.0f - (500.0f * Settings.scale), Settings.HEIGHT / 2.0f, card1);
//        card1.current_x = Settings.WIDTH / 2.0f;
//        card1.current_y = Settings.HEIGHT / 2.0f;
//        card2.current_x = Settings.WIDTH / 2.0f;
//        card2.current_y = Settings.HEIGHT / 2.0f;
//        card3.current_x = Settings.WIDTH / 2.0f;
//        card3.current_y = Settings.HEIGHT / 2.0f;
//        BoosterFieldPatch.betterEquipBooster(card2, new SubroutineDraw());
//        BoosterFieldPatch.betterEquipBooster(card2, new SubroutineDraw());
//        BoosterFieldPatch.betterEquipBooster(card2, new ScrapBooster());
//        button = (ReflectionHacks.getPrivateInherited(this, BoosterTutorial.class, "button"));
//    }
//
//    @Override
//    public void update() {
//        this.button.update();
//        if (button.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
//            if (currentSlot < maxSlots) {
//                currentSlot++;
//                CInputActionSet.proceed.unpress();
//                button.hb.clicked = false;
//                CardCrawlGame.sound.play("DECK_OPEN");
//                ReflectionHacks.setPrivateInherited(this, BoosterTutorial.class, "body", MSG[currentSlot]);
//                switch (currentSlot) {
//                    default:
//                        break;
//                    case 2:
//                        ReflectionHacks.setPrivateInherited(this, BoosterTutorial.class, "c", card2);
//                        break;
//                    case 3:
//                        ReflectionHacks.setPrivateInherited(this, BoosterTutorial.class, "c", card3);
//                        break;
//                    case 4:
//                        type = TipType.COMBAT;
//                        ReflectionHacks.setPrivateInherited(this, BoosterTutorial.class, "c", null);
//                }
//            } else {
//                super.update();
//                AbstractDungeon.effectList.clear();
//                AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
//            }
//        }
//    }
//
//    @Override
//    public void render(SpriteBatch sb) {
//        if (currentSlot == 4) {
//            sb.setColor(Color.WHITE);
//            sb.draw(img, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, Settings.scale * 3.0F, Settings.scale * 3.0F, 0.0F);
//        }
//        super.render(sb);
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
//    public static class ShowBoosterTutorialPatch {
//        public static void Postfix(AbstractPlayer __instance) {
//            if (__instance instanceof M10Robot && !M10RobotMod.boosterTutorialSeen) {
//                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.FTUE) {
//                    M10RobotMod.m10RobotConfig.setBool(M10RobotMod.BOOSTER_TUTORIAL_SEEN, true);
//                    M10RobotMod.boosterTutorialSeen = true;
//                    M10RobotMod.showBoosterTutorialButton.toggle.enabled = false;
//                    try { M10RobotMod.m10RobotConfig.save(); } catch (IOException e) { e.printStackTrace(); }
//                    AbstractDungeon.ftue = new BoosterTutorial();
//                }
//            }
//        }
//    }
//}