package jp.tsukineko.swordmod.entities.slash;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderEntitySlash extends RenderEntity{

    public static final ResourceLocation texture = new ResourceLocation("sword_mod:textures/entity/entity_slash_texture.png");

    public RenderEntitySlash(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }

    @Nullable
    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        renderOffsetAABB(entity.getEntityBoundingBox(), x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
