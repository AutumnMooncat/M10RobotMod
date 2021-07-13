package M10Robot.cardModifiers;

import java.util.ArrayList;
import java.util.Arrays;

public class AbstractBoosterPriorityLookup {
    private static final ArrayList<Class<?>> classPriority = new ArrayList<>(Arrays.asList(
            TempBlockModifier.class,
            TempDamageModifier.class,
            TempMagicNumberModifier.class,
            GainBlockEffect.class,
            DealDamageEffect.class,
            DealDamageToAllEnemiesEffect.class,
            ApplyPoisonEffect.class,
            DrawCardEffect.class,
            GainEnergyEffect.class,
            LoseEnergyEffect.class,
            HealHPEffect.class,
            LoseHPEffect.class,
            GainFocusEffect.class,
            LoseFocusEffect.class,
            GainStrengthEffect.class,
            LoseStrengthEffect.class,
            GainDexterityEffect.class,
            LoseDexterityEffect.class,
            GainOrbSlotEffect.class,
            LoseOrbSlotEffect.class,
            GainRecoilEffect.class,
            LoseRecoilEffect.class,
            TempRefundModifier.class));

    public static int getPriorityIndex(AbstractBoosterModifier mod) {
        int i = 0;
        for (Class<?> c : classPriority) {
            if (mod.getClass().equals(c)) {
                return i - classPriority.size();
            }
            i++;
        }
        return 100;
    }
}
