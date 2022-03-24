package M10Robot.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public interface BonusDamagePower {
    int modifyDamageBeforeBlock(AbstractCreature target, DamageInfo info, int damage);
}
