package M10Robot.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnBlockDamagePower {
     void onPartialBlock(DamageInfo info, int initialDamageAmount, int initialBlock);
    void onFullyBlock(DamageInfo info, int initialDamageAmount, int initialBlock);
}
