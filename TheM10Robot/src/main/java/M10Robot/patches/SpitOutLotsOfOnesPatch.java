//package M10Robot.patches;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.MathUtils;
//import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.NewExpr;
//
//public class SpitOutLotsOfOnesPatch {
//    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
//    public static class SoManyNumbersPlz {
//        @SpireInstrumentPatch
//        public static ExprEditor patch() {
//            return new ExprEditor() {
//                @Override
//                //Method call is basically the equivalent of a methodcallmatcher of an insert patch, checks the edit method against every method call in the function you#re patching
//                public void edit(NewExpr m) throws CannotCompileException {
//                    //If the method is from the class AnimationState and the method is called update
//                    if (m.getClassName().equals(StrikeEffect.class.getName())) {
//                        m.replace("{" +
//                                "$4 = M10Robot.patches.SpitOutLotsOfOnesPatch.zoom($1, $4);" +
//                                "$_ = $proceed($$);" +
//                                "}");
//                    }
//                }
//            };
//        }
//    }
//
//    public static final String[] strings = {"Bam!","Pow!","Biff!","Bonk!","Whack!","Wham!"};
//    public static String randomString() {
//        return strings[MathUtils.random(strings.length-1)];
//    }
//
//    public static int zoom(AbstractCreature targetHit, int input) {
//        if (input <= 1) {
//            return input;
//        }
//        input--;
//        int finalDamage = input;
//        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
//            int hits = finalDamage;
//            float timer;
//            final float timePerHit = 0.1f;
//            @Override
//            public void update() {
//                timer += Gdx.graphics.getDeltaTime();
//                if (timer >= timePerHit) {
//                    timer = 0;
//                    AbstractDungeon.effectList.add(new StrikeEffect(targetHit, targetHit.hb.cX, targetHit.hb.cY, randomString()));
//                    hits --;
//                }
//                if (hits <= 0) {
//                    this.isDone = true;
//                }
//            }
//        });
//        return 1;
//    }
//}
