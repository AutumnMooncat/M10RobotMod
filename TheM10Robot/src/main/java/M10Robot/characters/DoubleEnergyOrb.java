package M10Robot.characters;

import M10Robot.patches.RenderButtonOnCardPatch;
import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.HashMap;

public class DoubleEnergyOrb extends CustomEnergyOrb {
    private static final int SECOND_ORB_W = 128;
    private static final int PRIMARY_ORB_W = 128;
    private static final float SECOND_ORB_IMG_SCALE = 0.75F * Settings.scale;
    private static final float PRIMARY_ORB_IMG_SCALE = 1.15F * Settings.scale;
    private static final float X_OFFSET = 80f * Settings.scale;
    private static final float Y_OFFSET = 40f * Settings.scale;
    private static final float MAX_LEAN_PER_LAYER = 4f * Settings.scale;
    private static final float MAX_LEAN_AT_DISTANCE = 500f * Settings.scale;
    protected Texture secondBaseLayer;
    protected Texture[] secondEnergyLayers;
    protected Texture[] secondNoEnergyLayers;
    protected float[] secondLayerSpeeds;
    protected float[] secondAngles;
    protected float[] primaryXLean;
    protected float[] primaryYLean;
    protected float[] secondaryXLean;
    protected float[] secondaryYLean;

    public DoubleEnergyOrb(String[] orbTexturePaths, String orbVfxPath, float[] layerSpeeds) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        if (orbTexturePaths != null && orbVfxPath != null) {
            assert orbTexturePaths.length >= 3;

            assert orbTexturePaths.length % 2 == 1;

            int middleIdx = orbTexturePaths.length / 2;
            System.out.println(middleIdx);
            this.secondEnergyLayers = new Texture[middleIdx];
            this.secondNoEnergyLayers = new Texture[middleIdx];
            this.secondBaseLayer = ImageMaster.loadImage(orbTexturePaths[middleIdx]);

            for(int i = 0; i < middleIdx; ++i) {
                this.secondEnergyLayers[i] = ImageMaster.loadImage(orbTexturePaths[i]);
                this.secondNoEnergyLayers[i] = ImageMaster.loadImage(orbTexturePaths[i + middleIdx + 1]);
            }

            this.orbVfx = ImageMaster.loadImage(orbVfxPath);
        } else {
            this.secondEnergyLayers = new Texture[5];
            this.secondNoEnergyLayers = new Texture[5];
            this.secondBaseLayer = ImageMaster.ENERGY_RED_LAYER6;
            this.secondEnergyLayers[0] = ImageMaster.ENERGY_RED_LAYER1;
            this.secondEnergyLayers[1] = ImageMaster.ENERGY_RED_LAYER2;
            this.secondEnergyLayers[2] = ImageMaster.ENERGY_RED_LAYER3;
            this.secondEnergyLayers[3] = ImageMaster.ENERGY_RED_LAYER4;
            this.secondEnergyLayers[4] = ImageMaster.ENERGY_RED_LAYER5;
            this.secondNoEnergyLayers[0] = ImageMaster.ENERGY_RED_LAYER1D;
            this.secondNoEnergyLayers[1] = ImageMaster.ENERGY_RED_LAYER2D;
            this.secondNoEnergyLayers[2] = ImageMaster.ENERGY_RED_LAYER3D;
            this.secondNoEnergyLayers[3] = ImageMaster.ENERGY_RED_LAYER4D;
            this.secondNoEnergyLayers[4] = ImageMaster.ENERGY_RED_LAYER5D;
        }

        if (layerSpeeds == null) {
            layerSpeeds = new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F};
        }

        this.secondLayerSpeeds = layerSpeeds;
        this.secondAngles = new float[this.secondLayerSpeeds.length];
        this.primaryXLean = new float[this.energyLayers.length];
        this.primaryYLean = new float[this.energyLayers.length];
        this.secondaryXLean = new float[this.secondEnergyLayers.length];
        this.secondaryYLean = new float[this.secondEnergyLayers.length];

        assert this.secondEnergyLayers.length == this.secondLayerSpeeds.length;

    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    public void updateOrb(int energyCount) {
        super.updateOrb(energyCount);
        int d = this.secondAngles.length;
        for (int i = 0 ; i < this.secondAngles.length ; i++) {
            if (energyCount == 0) {
                this.secondAngles[i] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d-1-i] / 4.0F;
            } else {
                this.secondAngles[i] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d-1-i];
            }
        }
    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        calculateLeanValues(current_x, current_y, enabled);
        sb.setColor(Color.WHITE);
        int i;
        if (enabled) {
            for(i = 0; i < this.secondEnergyLayers.length; ++i) {
                sb.draw(this.secondEnergyLayers[i], current_x + secondaryXLean[i] + X_OFFSET - SECOND_ORB_W /2F, current_y + secondaryYLean[i] + Y_OFFSET - SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, SECOND_ORB_W, SECOND_ORB_W, false, false);
            }
        } else {
            for(i = 0; i < this.secondNoEnergyLayers.length; ++i) {
                sb.draw(this.secondNoEnergyLayers[i], current_x + secondaryXLean[i] + X_OFFSET - SECOND_ORB_W /2F, current_y + secondaryYLean[i] + Y_OFFSET - SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, SECOND_ORB_W, SECOND_ORB_W, false, false);
            }
        }
        sb.draw(this.secondBaseLayer, current_x + X_OFFSET - SECOND_ORB_W /2F, current_y + Y_OFFSET - SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W /2F, SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_IMG_SCALE, SECOND_ORB_IMG_SCALE, 0.0F, 0, 0, SECOND_ORB_W, SECOND_ORB_W, false, false);

        if (enabled) {
            for(i = 0; i < this.energyLayers.length; ++i) {
                sb.draw(this.energyLayers[i], current_x + primaryXLean[i] - PRIMARY_ORB_W/2F, current_y + primaryYLean[i] - PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W, PRIMARY_ORB_W, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        } else {
            for(i = 0; i < this.noEnergyLayers.length; ++i) {
                sb.draw(this.noEnergyLayers[i], current_x + primaryXLean[i] - PRIMARY_ORB_W/2F, current_y  + primaryYLean[i] - PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W, PRIMARY_ORB_W, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }

        sb.draw(this.baseLayer, current_x - PRIMARY_ORB_W/2F, current_y - PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W/2F, PRIMARY_ORB_W, PRIMARY_ORB_W, PRIMARY_ORB_IMG_SCALE, PRIMARY_ORB_IMG_SCALE, 0.0F, 0, 0, 128, 128, false, false);
    }

    protected void calculateLeanValues(float x, float y, boolean enabled) {
        if (!enabled) {
            for (int i = 0 ; i < energyLayers.length ; i++) {
                primaryXLean[i] -= (Math.min(Math.abs(primaryXLean[i]), i * MAX_LEAN_PER_LAYER * PRIMARY_ORB_IMG_SCALE) * Math.signum(primaryXLean[i]));
                primaryYLean[i] -= (Math.min(Math.abs(primaryYLean[i]), i * MAX_LEAN_PER_LAYER * PRIMARY_ORB_IMG_SCALE) * Math.signum(primaryYLean[i]));
                secondaryXLean[i] -= (Math.min(Math.abs(secondaryXLean[i]), i * MAX_LEAN_PER_LAYER * SECOND_ORB_IMG_SCALE) * Math.signum(secondaryXLean[i]));
                secondaryYLean[i] -= (Math.min(Math.abs(secondaryYLean[i]), i * MAX_LEAN_PER_LAYER * SECOND_ORB_IMG_SCALE) * Math.signum(secondaryYLean[i]));
            }
        } else {
            Vector2 pVec = new Vector2(InputHelper.mX - x, InputHelper.mY - y);
            Vector2 sVec = new Vector2(InputHelper.mX - (x+X_OFFSET), InputHelper.mY - (y+Y_OFFSET));
            float primaryDist = Math.min(1, pVec.len()/MAX_LEAN_AT_DISTANCE);
            float secondaryDist = Math.min(1, sVec.len()/MAX_LEAN_AT_DISTANCE);
            pVec.nor();
            sVec.nor();
            for (int i = 0 ; i < energyLayers.length ; i++) {
                primaryXLean[i] = i * MAX_LEAN_PER_LAYER * primaryDist * pVec.x * PRIMARY_ORB_IMG_SCALE;
                primaryYLean[i] = i * MAX_LEAN_PER_LAYER * primaryDist * pVec.y * PRIMARY_ORB_IMG_SCALE;
                secondaryXLean[i] = i * MAX_LEAN_PER_LAYER * secondaryDist * sVec.x * SECOND_ORB_IMG_SCALE;
                secondaryYLean[i] = i * MAX_LEAN_PER_LAYER * secondaryDist * sVec.y * SECOND_ORB_IMG_SCALE;
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class DoubleOrbField {
        public static SpireField<Boolean> orb = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = EnergyPanel.class, method = "renderVfx")
    public static class FlashSecondOrbPatch
    {
        @SpirePrefixPatch
        public static void flashSecondOrb(EnergyPanel __instance, SpriteBatch sb, Texture ___gainEnergyImg, Color ___energyVfxColor, float ___energyVfxScale, float ___energyVfxAngle) {
            if (EnergyPanel.energyVfxTimer != 0.0F && DoubleOrbField.orb.get(AbstractDungeon.player)) {
                sb.setBlendFunction(770, 1);
                sb.setColor(___energyVfxColor);
                sb.draw(___gainEnergyImg, __instance.current_x + X_OFFSET - SECOND_ORB_W, __instance.current_y + Y_OFFSET - SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_W*2F, SECOND_ORB_W*2F, ___energyVfxScale*SECOND_ORB_IMG_SCALE/PRIMARY_ORB_IMG_SCALE, ___energyVfxScale*SECOND_ORB_IMG_SCALE/PRIMARY_ORB_IMG_SCALE, ___energyVfxAngle - 50.0F, 0, 0, SECOND_ORB_W*2, SECOND_ORB_W*2, true, false);
                sb.draw(___gainEnergyImg, __instance.current_x + X_OFFSET - SECOND_ORB_W, __instance.current_y + Y_OFFSET - SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_W, SECOND_ORB_W*2F, SECOND_ORB_W*2F, ___energyVfxScale*SECOND_ORB_IMG_SCALE/PRIMARY_ORB_IMG_SCALE, ___energyVfxScale*SECOND_ORB_IMG_SCALE/PRIMARY_ORB_IMG_SCALE, -___energyVfxAngle, 0, 0, SECOND_ORB_W*2, SECOND_ORB_W*2, false, false);
                sb.setBlendFunction(770, 771);
            }
        }
    }
}