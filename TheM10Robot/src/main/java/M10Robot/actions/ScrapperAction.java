package M10Robot.actions;

import M10Robot.orbs.ExtraOrbFields;
import M10Robot.powers.SpikesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;

public class ScrapperAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_FAST;
    private final int bonus;
    private final boolean applyBonus;

    public ScrapperAction(int amount, int bonus, boolean applyBonus) {
        this.actionType = ActionType.DAMAGE;
        this.amount = amount;
        this.duration = DURATION;
        this.bonus = bonus;
        this.applyBonus = applyBonus;
    }

    public void update() {
        if (duration == DURATION) {
            if (!AbstractDungeon.player.orbs.isEmpty()) {
                AbstractOrb orb = AbstractDungeon.player.orbs.get(0);
                int spikes = amount;
                if (applyBonus) {
                    spikes += (bonus * ExtraOrbFields.ExtraFields.timesUpgraded.get(orb));
                }
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpikesPower(AbstractDungeon.player, spikes)));
                if (!(orb instanceof EmptyOrbSlot)) {
                    this.addToTop(new EvokeOrbAction(1));
                    CardCrawlGame.sound.play("ATTACK_FIRE");

                    int i;
                    for(i = 0; i < 15; ++i) { //75
                        AbstractDungeon.effectsQueue.add(new FlameParticleEffect(orb.cX, orb.cY));
                    }

                    for(i = 0; i < 4; ++i) { //20
                        AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(orb.cX, orb.cY));
                    }
                }
            } else {
                this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpikesPower(AbstractDungeon.player, amount)));
            }
        }
        tickDuration();
    }
}
