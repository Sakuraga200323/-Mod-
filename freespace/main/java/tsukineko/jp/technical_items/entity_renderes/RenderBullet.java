package tsukineko.jp.technical_items.entity_renderes;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tsukineko.jp.technical_items.TechnicalItemsMod;
import tsukineko.jp.technical_items.entities.EntityBullet;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderBullet<E extends Entity> extends Render<EntityBullet> {

    public static final ResourceLocation texture = new ResourceLocation(TechnicalItemsMod.MOD_ID, "textures/entity/guided_bullet.png");

    public RenderBullet(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(EntityBullet entity) {
        return texture;
    }

    @Override
    public void doRender(EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(texture);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}

