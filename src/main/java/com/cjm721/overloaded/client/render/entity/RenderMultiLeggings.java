package com.cjm721.overloaded.client.render.entity;

import com.cjm721.overloaded.Overloaded;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static com.cjm721.overloaded.Overloaded.MODID;

public class RenderMultiLeggings extends AbstractRenderMultiArmor {

  public static RenderMultiLeggings INSTANCE;

  public RenderMultiLeggings(BipedModel baseModel) {
    super(baseModel, 10f);
    IBakedModel leftLeg = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(Overloaded.MODID, "item/armor/multi_left_leg"));
    IBakedModel rightLeg = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(Overloaded.MODID, "item/armor/multi_right_leg"));

//    this.bipedLeftLeg.addChild(new ModelRenderOBJ(this, leftLeg));
    this.bipedRightLeg.addChild(new ModelRenderOBJ(this, rightLeg));
  }
}
