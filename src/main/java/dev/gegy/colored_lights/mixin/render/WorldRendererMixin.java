package dev.gegy.colored_lights.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import dev.gegy.colored_lights.ColoredLightCorner;
import dev.gegy.colored_lights.mixin.render.chunk.BuiltChunkStorageAccess;
import dev.gegy.colored_lights.render.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 970)
public class WorldRendererMixin {
    @Inject(method = "scheduleChunkRender", at = @At("HEAD"))
    private void scheduleChunkRender(int x, int y, int z, boolean important, CallbackInfo ci) {
        ColoredLightAdapter.scheduleChunkRender(x, y, z);
    }

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;startDrawing()V", shift = At.Shift.AFTER))
    private void prepareRenderLayer(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci) {
        ColoredLightAdapter.prepareRenderLayer();
    }

    @Inject(
            method = "renderLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;set(FFF)V")
    )
    private void prepareRenderChunk(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci, @Local ChunkBuilder.BuiltChunk builtChunk) {
        ColoredLightAdapter.prepareRenderChunk(builtChunk);
    }

    @Inject(method = "renderLayer", at = @At("RETURN"))
    private void finishRenderLayer(RenderLayer layer, MatrixStack transform, double cameraX, double cameraY, double cameraZ, Matrix4f projection, CallbackInfo ci) {
        ColoredLightAdapter.finishRenderLayer();
    }
}