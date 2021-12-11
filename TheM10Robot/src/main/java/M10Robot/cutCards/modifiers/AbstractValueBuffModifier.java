package M10Robot.cutCards.modifiers;

public abstract class AbstractValueBuffModifier extends AbstractBoosterModifier {

    public AbstractValueBuffModifier(String ID, int increase) {
        super(ID);
        this.amount = increase;
    }

}
