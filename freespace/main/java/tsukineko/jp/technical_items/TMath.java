package tsukineko.jp.technical_items;

import net.minecraft.entity.player.EntityPlayer;

public class TMath {

    public static float extendsToRadian(float rotation){
        return rotation / 180.0f * (float) Math.PI;
    }
    public static float extendsToRotation(float radian){ return radian * 180.0f / (float) Math.PI; }

    public static float getRotationYawRad(EntityPlayer player){
        return extendsToRadian(player.getRotationYawHead());
    }

    public static float getRotationPitchRad(EntityPlayer player){
        return extendsToRadian(player.rotationPitch);
    }

    public static float fixRotationYaw(float num){
        if(num > 180)
            num -= 360;
        if(num < -180)
            num += 360;
        return num;
    }

    public static float fixRotationPitch(float num){
        if(num > 90)
            num -= 180;
        if(num < -90)
            num += 180;
        return num;
    }

    public static float addRotationYawNum(EntityPlayer p, float num){
        return fixRotationYaw(p.getRotationYawHead() + num);
    }

    public static float addRotationPitchNum(EntityPlayer p, float num){
        return fixRotationPitch(p.rotationPitch + num);
    }

    public static float addRotationYawNumRad(EntityPlayer p, float num){
        return extendsToRadian(fixRotationYaw(p.getRotationYawHead() + num));
    }

    public static float addRotationPitchNumRad(EntityPlayer p, float num){
        return extendsToRadian(fixRotationPitch(p.rotationPitch + num));
    }

    public static double sin(float rotation){
        return Math.sin(TMath.extendsToRadian(rotation));
    }

    public static double cos(float rotation){
        return Math.cos(TMath.extendsToRadian(rotation));
    }

    public static double tan(float rotation){
        return Math.tan(TMath.extendsToRadian(rotation));
    }

}
