package dev.gegy.colored_lights.mixin.render;

import dev.gegy.colored_lights.render.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 970)
public class CompatibleWorldRendererMixin implements ColoredLightWorldRenderer, ColoredLightReader {
    @Inject(method = "render", at = @At("HEAD"))
    private void beforeRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        ColoredLightAdapter.beforeRender(tickDelta);
    }

//    @Inject(
//            method = "renderEntity",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"),
//            locals = LocalCapture.PRINT
//    )
//    private void beforeRenderEntity(
//            Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci,
//            @Local(ordinal = 3) double entityX, @Local(ordinal = 4) double entityY, @Local(ordinal = 5) double entityZ
//    ) {
//        ColoredLightImpl.beforeRenderEntity(entityX, entityY, entityZ);
//    }
//
//    @Inject(method = "renderEntity", at = @At("RETURN"))
//    private void afterRenderEntity(
//            Entity entity, double cameraX, double cameraY, double cameraZ,
//            float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci
//    ) {
//        ColoredLightImpl.afterRenderEntity();
//    }

    @Override
    public void read(double x, double y, double z, ColorConsumer consumer) {
        ColoredLightAdapter.read(x, y, z, consumer);
    }

    @Override
    public ChunkLightColorUpdater getChunkLightColorUpdater() {
        return ColoredLightAdapter.getChunkLightColorUpdater();
    }
}
