package dev.gegy.colored_lights.mixin.render.sodium;

import dev.gegy.colored_lights.render.*;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SodiumWorldRenderer.class, priority = 970, remap = false)
public class SodiumWorldRendererMixin {
    @Shadow private RenderSectionManager renderSectionManager;

    @Inject(method = "scheduleRebuildForChunk", at = @At("HEAD"))
    private void scheduleChunkRender(int x, int y, int z, boolean important, CallbackInfo ci) {
        ColoredLightAdapter.scheduleChunkRender(x, y, z);
    }

    @Inject(method = "drawChunkLayer", at = @At("HEAD"))
    private void prepareRenderLayer(RenderLayer renderLayer, ChunkRenderMatrices matrices, double x, double y, double z, CallbackInfo ci) {
        ColoredLightAdapter.prepareRenderLayer();
    }

    @Inject(
            method = "drawChunkLayer",
            at = @At("TAIL") // Inject at the end of the method
    )
    private void prepareRenderChunk(RenderLayer renderLayer, ChunkRenderMatrices matrices, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        for (ChunkBuilder.BuiltChunk builtChunk : MinecraftClient.getInstance().worldRenderer.chunks.chunks) {
            ColoredLightAdapter.prepareRenderChunk(builtChunk);
        }
    }
}
