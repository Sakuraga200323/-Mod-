package tsukineko.jp.technical_items.entity_renderes;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tsukineko.jp.technical_items.TechnicalItemsMod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderGuidedBullet<E extends Entity> extends Render<Entity> {

    public static final ResourceLocation texture = new ResourceLocation(TechnicalItemsMod.MOD_ID, "textures/entities/guided_bullet.png");
    private final float scale;

    public RenderGuidedBullet(RenderManager renderManager, float scaleIn) {
        super(renderManager);
        this.scale = scaleIn;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(getEntityTexture(entity));

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
