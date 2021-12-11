package M10Robot.actions;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BalanceCurrentsAction extends AbstractGameAction {

    public BalanceCurrentsAction(AbstractCreature source, int amountPer) {
        this.source = source;
        this.amount = amountPer;
    }

    @Override
    public void update() {
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                int total = 0, debuff = 0;
                for (AbstractPower pow : aM.powers) {
                    //Don't count invisible powers
                    if (!(pow instanceof InvisiblePower)) {
                        total++;
                        if (pow.type == AbstractPower.PowerType.DEBUFF) {
                            debuff++;
                        }
                    }
                }
                if (total > 0) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.FIRE));
                    aM.damage(new DamageInfo(source, amount*total, DamageInfo.DamageType.HP_LOSS));

                }
                if (debuff > 0) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SHIELD));
                    aM.addBlock(amount*debuff);
                }
            }
        }
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
        this.isDone = true;
    }
}
