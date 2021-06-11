package M10Robot.variables;

import M10Robot.cards.abstractCards.AbstractModdedCard;
import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

import static M10Robot.M10RobotMod.makeID;

public class DefaultInvertedNumber extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.

    @Override
    public String key() {
        return makeID("InvertedNumber");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractModdedCard) card).isInvertedNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractModdedCard) card).invertedNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractModdedCard) card).baseInvertedNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractModdedCard) card).upgradedInvertedNumber;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Settings.RED_TEXT_COLOR;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Settings.GREEN_TEXT_COLOR;
    }

    @Override
    public Color getUpgradedColor() {
        return Settings.RED_TEXT_COLOR;
    }
}