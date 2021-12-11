//package M10Robot.patches;
//
//import M10Robot.characters.M10Robot;
//import com.badlogic.gdx.math.MathUtils;
//import com.evacipated.cardcrawl.modthespire.lib.ByRef;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//
//public class MonsterBlockPatches {
//
//    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
//    public static class AddBlockPrefixPatch {
//        //public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
//        @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
//        public static SpireReturn<?> BlockReader(AbstractCreature __instance, @ByRef int[] blockAmount) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
//        {
//            //To make sure it doesn't interfere with other mods
//            if(AbstractDungeon.player.chosenClass == M10Robot.Enums.THE_MIO_ROBOT) {
//                if (__instance instanceof AbstractMonster) {
//                    float tmp = blockAmount[0];
//                    //logger.info("Intro block:" + tmp);
//                    for (AbstractPower p : __instance.powers) {
//                        //logger.info("Checking power:" + p);
//                        tmp = p.modifyBlock(tmp);
//                        //logger.info("Current block:" + tmp);
//                    }
//                    //logger.info("Final block:" + tmp);
//                    blockAmount[0] = MathUtils.floor(tmp);
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }
//}
