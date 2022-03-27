package M10Robot.actions;

import M10Robot.orbs.AbstractCustomOrb;
import M10Robot.orbs.ExtraOrbFields;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.ArrayList;

public class UpgradeOrbsAction extends AbstractGameAction {
    private static final float PASSIVE_INCREASE = 0.25f;
    private static final float EVOKE_INCREASE = 0.25f;
    private AbstractOrb orb;
    private boolean allOrbs;

    public UpgradeOrbsAction(boolean allOrbs, int upgrades) {
        this.allOrbs = allOrbs;
        this.amount = upgrades;
    }

    public UpgradeOrbsAction(AbstractOrb orb, int upgrades) {
        this.orb = orb;
        this.amount = upgrades;
    }

    @Override
    public void update() {
        if (orb == null) {
            if (allOrbs) {
                for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    upgrade(o);
                }
            } else {
                ArrayList<AbstractOrb> validOrbs = new ArrayList<>();
                for (AbstractOrb o : AbstractDungeon.player.orbs) {
                    if (!(o instanceof EmptyOrbSlot)) {
                        validOrbs.add(o);
                    }
                }
                if (!validOrbs.isEmpty()) {
                    upgrade(validOrbs.get(AbstractDungeon.cardRng.random(validOrbs.size() - 1)));
                }
            }
        } else {
            upgrade(orb);
        }
        this.isDone = true;
    }

    private static boolean usePassive, useEvoke;
    private void upgrade(AbstractOrb o) {
        if (o instanceof AbstractCustomOrb) {
            for (int i = 0 ; i < amount ; i++) {
                ((AbstractCustomOrb) o).upgrade();
            }
            ((AbstractCustomOrb) o).playAnimation(((AbstractCustomOrb) o).successImage, AbstractCustomOrb.LONG_ANIM);
            CardCrawlGame.sound.play("ATTACK_FIRE");

            int i;
            for(i = 0; i < 15; ++i) { //75
                AbstractDungeon.effectsQueue.add(new FlameParticleEffect(o.cX, o.cY));
            }

            for(i = 0; i < 4; ++i) { //20
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(o.cX, o.cY));
            }
        } else if (!(o instanceof EmptyOrbSlot)) {
            usePassive = useEvoke = false;
            try {
                ClassPool pool = Loader.getClassPool();
                CtMethod ctUD = pool.get(o.getClass().getName()).getDeclaredMethod("updateDescription");
                ctUD.instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) {
                        if (f.getFieldName().equals("passiveAmount") && !f.isWriter()) {
                            usePassive = true;
                        }
                        if (f.getFieldName().equals("evokeAmount") && !f.isWriter()) {
                            useEvoke = true;
                        }
                    }
                });
                /*CtMethod ctRT = pool.get(o.getClass().getName()).getDeclaredMethod("renderText");
                ctRT.instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) {
                        if (f.getFieldName().equals("passiveAmount") && !f.isWriter()) {
                            usePassive = true;
                        }
                        if (f.getFieldName().equals("evokeAmount") && !f.isWriter()) {
                            useEvoke = true;
                        }
                    }
                });*/
            } catch (Exception ignored) {}
            if (usePassive || useEvoke) {
                if (usePassive) {
                    int p = ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "basePassiveAmount");
                    if (ExtraOrbFields.ExtraFields.passiveIncrease.get(o) == -1) {
                        ExtraOrbFields.ExtraFields.passiveIncrease.set(o, (int) Math.ceil(p * PASSIVE_INCREASE));
                    }
                    ReflectionHacks.setPrivate(o, AbstractOrb.class, "basePassiveAmount", p + (ExtraOrbFields.ExtraFields.passiveIncrease.get(o) * amount));
                    o.passiveAmount += ExtraOrbFields.ExtraFields.passiveIncrease.get(o) * amount;
                }
                if (useEvoke) {
                    int e = ReflectionHacks.<Integer>getPrivate(o, AbstractOrb.class, "baseEvokeAmount");
                    if (ExtraOrbFields.ExtraFields.evokeIncrease.get(o) == -1) {
                        ExtraOrbFields.ExtraFields.evokeIncrease.set(o, (int) Math.ceil(e * EVOKE_INCREASE));
                    }
                    ReflectionHacks.setPrivate(o, AbstractOrb.class, "baseEvokeAmount", e + (ExtraOrbFields.ExtraFields.evokeIncrease.get(o) * amount));
                    o.evokeAmount += ExtraOrbFields.ExtraFields.evokeIncrease.get(o) * amount;
                }
                ExtraOrbFields.upgradeCount(o, amount);
                o.updateDescription();
                CardCrawlGame.sound.play("ATTACK_FIRE");

                int i;
                for(i = 0; i < 15; ++i) { //75
                    AbstractDungeon.effectsQueue.add(new FlameParticleEffect(o.cX, o.cY));
                }

                for(i = 0; i < 4; ++i) { //20
                    AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(o.cX, o.cY));
                }
            }
        }
    }

}
