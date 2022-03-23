package M10Robot.augments;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import M10Robot.M10RobotMod;
import M10Robot.characters.M10Robot;
import basemod.AutoAdd;

public class AugmentHelper {
    public static void register() {
        new AutoAdd(M10RobotMod.getModID())
                .packageFilter("M10Robot.augments")
                .any(AbstractAugment.class, (info, abstractAugment) -> {
                    CardAugmentsMod.registerAugment(abstractAugment);});
        CardAugmentsMod.registerOrbCharacter(M10Robot.Enums.THE_MIO_ROBOT);
    }
}
