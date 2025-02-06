package dev.gegy.colored_lights.mixin.render.sodium;

import dev.gegy.colored_lights.render.*;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderSectionManager.class, remap = false)
public class RenderSectionManagerMixin {

//    @Inject(
//            method = "renderLayer",
//            at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/render/chunk/ChunkRenderer;render(Lme/jellysquid/mods/sodium/client/render/chunk/ChunkRenderMatrices;Lme/jellysquid/mods/sodium/client/gl/device/CommandList;Lme/jellysquid/mods/sodium/client/render/chunk/lists/ChunkRenderListIterable;Lme/jellysquid/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;Lme/jellysquid/mods/sodium/client/render/viewport/CameraTransform;)V")
//    )
//    private void prepareRenderChunk(ChunkRenderMatrices matrices, TerrainRenderPass pass, double x, double y, double z, CallbackInfo ci, @Local ChunkBuilder.BuiltChunk builtChunk) {
//        var chunkLightColors = this.chunkLightColors;
//        if (chunkLightColors == null) return;
//
//        long colors = ((ColoredLightBuiltChunk) builtChunk).getPackedChunkLightColors();
//        if (this.lastChunkLightColors != colors) {
//            this.lastChunkLightColors = colors;
//
//            int colorsHigh = (int) (colors >>> 32);
//            int colorsLow = (int) colors;
//            chunkLightColors.set(colorsHigh, colorsLow);
//            chunkLightColors.upload();
//        }
//    }

    @Inject(method = "renderLayer", at = @At("RETURN"))
    private void finishRenderLayer(ChunkRenderMatrices matrices, TerrainRenderPass pass, double x, double y, double z, CallbackInfo ci) {
        ColoredLightAdapter.finishRenderLayer();
    }
}
