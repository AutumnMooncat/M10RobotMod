package M10Robot.augments;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.characters.M10Robot;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static M10Robot.M10RobotMod.makeID;

public class AugmentHelper {
    public static void register() {
        CardAugmentsMod.registerMod(M10RobotMod.getModID(), CardCrawlGame.languagePack.getUIString(makeID("ModConfigs")).TEXT[0]);
        new AutoAdd(M10RobotMod.getModID())
                .packageFilter("M10Robot.augments")
                .any(AbstractAugment.class, (info, abstractAugment) -> {
                    CardAugmentsMod.registerAugment(abstractAugment, M10RobotMod.getModID());});
        CardAugmentsMod.registerOrbCharacter(M10Robot.Enums.THE_MIO_ROBOT);
    }
}
