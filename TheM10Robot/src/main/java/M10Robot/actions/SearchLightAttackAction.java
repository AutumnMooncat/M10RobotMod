package M10Robot.actions;

import M10Robot.orbs.SearchlightOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import static M10Robot.orbs.AbstractCustomOrb.MED_ANIM;

public class SearchLightAttackAction extends AbstractGameAction {
    private final SearchlightOrb linkedOrb;
    private static final float DURATION = 0.1f;

    public SearchLightAttackAction(SearchlightOrb linkedOrb, AbstractCreature source, AbstractCreature target) {
        this.linkedOrb = linkedOrb;
        this.source = source;
        this.target = target;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (duration == DURATION) {
            linkedOrb.playAnimation(SearchlightOrb.ATTACK_IMG, MED_ANIM);
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            AbstractDungeon.effectList.add(new OrbFlareEffect(linkedOrb, OrbFlareEffect.OrbFlareColor.FROST));
            int damage = linkedOrb.passiveAmount;
            if (target.hasPower(LockOnPower.POWER_ID)) {
                damage = (int)(damage * 1.5F);
            }
            if (!target.isDeadOrEscaped()) {
                target.damage(new DamageInfo(source, damage, DamageInfo.DamageType.THORNS));
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
        }
        tickDuration();
    }
}
