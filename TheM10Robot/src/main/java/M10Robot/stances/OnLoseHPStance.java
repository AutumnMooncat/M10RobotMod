package M10Robot.stances;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnLoseHPStance {
    int onLoseHP(DamageInfo info, int damageAmount);
}
