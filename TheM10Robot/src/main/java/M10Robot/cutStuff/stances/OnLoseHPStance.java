package M10Robot.cutStuff.stances;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnLoseHPStance {
    int onLoseHP(DamageInfo info, int damageAmount);
}
