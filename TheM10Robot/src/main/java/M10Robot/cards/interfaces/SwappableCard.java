package M10Robot.cards.interfaces;

public interface SwappableCard {
    default void onSwapOut() {}

    default void onSwapIn() {}
}
