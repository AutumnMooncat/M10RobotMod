package M10Robot.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.HashMap;

public class RushdownAction extends AbstractGameAction {
    final float xB;
    final float speed = 100f * Settings.scale;
    final HashMap<AbstractMonster, Boolean> hitMap = new HashMap<>();
    final HashMap<AbstractMonster, Boolean> targetToTheRight = new HashMap<>();
    final boolean flipped;
//    final boolean hasThorns;
    boolean firstPass = true;
    boolean firstPhase = true;
    boolean secondPhase = false;
    float dx;
//    AbstractPower thornsPower;
    int[] damages;
    DamageInfo.DamageType damageType;

    public RushdownAction(AbstractCreature source, int[] damages, DamageInfo.DamageType damageType) {
        this.damages = damages;
        this.source = source;
        this.flipped = source.flipHorizontal;
        this.xB = source.drawX;
        this.damageType = damageType;
//        this.hasThorns = source.hasPower(ThornsPower.POWER_ID);
    }


    @Override
    public void update() {
        if (firstPass) {
            firstPass = false;
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped()) {
                    hitMap.put(aM, false);
                    targetToTheRight.put(aM, aM.hb.cX >= source.hb.cX);
                }
            }
//            if (hasThorns) {
//                thornsPower = source.getPower(ThornsPower.POWER_ID);
//            }
            if (flipped) {
                source.flipHorizontal = false;
            }
        }
        if (firstPhase) {
            dx = speed;
            source.drawX += dx;
            source.hb.move(source.hb.cX+dx,source.hb.cY);
            for (AbstractMonster aM : hitMap.keySet()) {
                if (!hitMap.get(aM) && targetToTheRight.get(aM) && source.hb.cX >= aM.hb.cX) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.BLUNT_HEAVY));
                    aM.damage(new DamageInfo(source, damages[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageType));
                    hitMap.put(aM, true);
//                    if (hasThorns) {
//                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SLASH_HORIZONTAL));
//                        aM.damage(new DamageInfo(source, thornsPower.amount, DamageInfo.DamageType.THORNS));
//                        thornsPower.flash();
//                    }
                }
            }
            if (source.drawX > Settings.WIDTH + (2 * speed)) {
                firstPhase = false;
                secondPhase = true;
                source.drawX -= (Settings.WIDTH + (4 * speed));
                source.hb.move(source.hb.cX-(Settings.WIDTH + (4 * speed)),source.hb.cY);
            }
        }
        if (secondPhase) {
            dx = limitMovement(speed, xB-source.drawX);
            source.drawX += dx;
            source.hb.move(source.hb.cX+dx,source.hb.cY);
            for (AbstractMonster aM : hitMap.keySet()) {
                if (!hitMap.get(aM) && !targetToTheRight.get(aM) && source.hb.cX >= aM.hb.cX) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.BLUNT_HEAVY));
                    aM.damage(new DamageInfo(source, damages[AbstractDungeon.getMonsters().monsters.indexOf(aM)], damageType));
                    hitMap.put(aM, true);
//                    if (hasThorns) {
//                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(aM.hb.cX, aM.hb.cY, AttackEffect.SLASH_HORIZONTAL));
//                        aM.damage(new DamageInfo(source, thornsPower.amount, DamageInfo.DamageType.THORNS));
//                        thornsPower.flash();
//                    }
                }
            }
            if (source.drawX == xB) {
                secondPhase = false;
                isDone = true;
            }
        }
        if (isDone) {
            if (flipped) {
                source.flipHorizontal = true;
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }

    public float limitMovement(float desiredSpeed, float maxSpeed) {
        return (Math.abs(desiredSpeed) > Math.abs(maxSpeed)) ? maxSpeed : desiredSpeed;
    }
}
