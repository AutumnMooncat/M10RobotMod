package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.HashMap;

public class SplitBeamActionOLD extends AbstractGameAction {
    DamageInfo.DamageType type;
    HashMap<AbstractMonster, Integer> hitMap;
    int[] damage;

    public SplitBeamActionOLD(AbstractCreature source, int[] damage, DamageInfo.DamageType damageType, int hits) {
        this.source = source;
        this.amount = hits;
        this.type = damageType;
        this.damage = damage;
        this.hitMap = new HashMap<>();
    }

    private SplitBeamActionOLD(AbstractCreature source, int[] damage, DamageInfo.DamageType damageType, int hits, HashMap<AbstractMonster, Integer> hitMap) {
        this.source = source;
        this.amount = hits;
        this.type = damageType;
        this.damage = damage;
        this.hitMap = hitMap;
    }

    @Override
    public void update() {
        if (amount > 0) {
            AbstractMonster mo = AbstractDungeon.getRandomMonster();
            hitMap.put(mo, hitMap.getOrDefault(mo, 0)+1);
            this.addToTop(new SplitBeamActionOLD(source, damage, damageType, amount-1, hitMap));
            this.addToTop(new DamageAction(mo, new DamageInfo(source, damage[AbstractDungeon.getMonsters().monsters.indexOf(mo)], type), AbstractGameAction.AttackEffect.NONE, true));
            this.addToTop(new VFXAction(new SmallLaserEffect(mo.hb.cX, mo.hb.cY, source.hb.cX, source.hb.cY), 0.1F));
            this.addToTop(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        } else {
            for (AbstractMonster mo : hitMap.keySet()) {
                if (hitMap.get(mo) > 1 && !mo.isDeadOrEscaped()) {
                    this.addToTop(new DamageAction(mo, new DamageInfo(source, damage[AbstractDungeon.getMonsters().monsters.indexOf(mo)], type), AbstractGameAction.AttackEffect.NONE, true));
                    this.addToTop(new VFXAction(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY), 0.1F));
                }
            }
        }
        this.isDone = true;
    }
}
