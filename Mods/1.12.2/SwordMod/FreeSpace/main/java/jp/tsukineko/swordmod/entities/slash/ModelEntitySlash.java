package jp.tsukineko.swordmod.entities.slash;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * ModelBat - Either Mojang or a mod author
 * Created using Tabula 7.1.0
 */
public class ModelEntitySlash extends ModelBase {
    public ModelRenderer shape13;

    public ModelEntitySlash() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shape13 = new ModelRenderer(this, 0, 0);
        this.shape13.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shape13.addBox(-0.5F, -20.0F, -0.5F, 1, 40, 1, 0.0F);
        this.setRotateAngle(shape13, 0.0F, 0.7853981633974483F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.shape13.offsetX, this.shape13.offsetY, this.shape13.offsetZ);
        GlStateManager.translate(this.shape13.rotationPointX * f5, this.shape13.rotationPointY * f5, this.shape13.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 1.0D, 2.0D);
        GlStateManager.translate(-this.shape13.offsetX, -this.shape13.offsetY, -this.shape13.offsetZ);
        GlStateManager.translate(-this.shape13.rotationPointX * f5, -this.shape13.rotationPointY * f5, -this.shape13.rotationPointZ * f5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.3F);
        this.shape13.render(f5);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
