package M10Robot.variables;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static M10Robot.M10RobotMod.makeID;

public class CurrentAmmoVariable extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.
    //public static boolean invertColor = false;

    @Override
    public String key() {
        return makeID("CurrentAmmo");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractModdedCard) card).isCurrentAmmoModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractModdedCard) card).currentAmmo;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractModdedCard) card).maxAmmo;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractModdedCard) card).upgradedMaxAmmo;
    }

}