package dev.gegy.colored_lights.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.gegy.colored_lights.ColoredLightCorner;
import dev.gegy.colored_lights.ColoredLights;
import dev.gegy.colored_lights.mixin.render.chunk.BuiltChunkStorageAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;

public class ColoredLightAdapter {
    private static final ColoredLightAdapter INSTANCE = new ColoredLightAdapter();

    private ColoredLightAdapter() {}

    public static ColoredLightAdapter getInstance() {
        return INSTANCE;
    }

    private static final ChunkLightColorUpdater chunkLightColorUpdater = new ChunkLightColorUpdater();
    private static final BlockPos.Mutable readBlockPos = new BlockPos.Mutable();
    private static GlUniform chunkLightColors;
    private static long lastChunkLightColors;


    public static void scheduleChunkRender(int x, int y, int z) {
        chunkLightColorUpdater.rerenderChunk(MinecraftClient.getInstance().world, MinecraftClient.getInstance().worldRenderer.chunks, x, y, z);
    }

    public static void prepareRenderLayer() {
        var shader = RenderSystem.getShader();
        chunkLightColors = ColoredLights.CHUNK_LIGHT_COLORS.get(shader);
        lastChunkLightColors = 0;
    }

    public static void prepareRenderChunk(ChunkBuilder.BuiltChunk builtChunk) {
        var chunkLightColors = ColoredLightAdapter.chunkLightColors;
        if (chunkLightColors == null) return;

        long colors = ((ColoredLightBuiltChunk) builtChunk).getPackedChunkLightColors();
        if (ColoredLightAdapter.lastChunkLightColors != colors) {
            ColoredLightAdapter.lastChunkLightColors = colors;

            int colorsHigh = (int) (colors >>> 32);
            int colorsLow = (int) colors;
            chunkLightColors.set(colorsHigh, colorsLow);
            chunkLightColors.upload();
        }
    }

    public static void finishRenderLayer() {
        ColoredLightAdapter.lastChunkLightColors = 0;

        var chunkLightColors = ColoredLightAdapter.chunkLightColors;
        if (chunkLightColors != null) {
            chunkLightColors.set(0, 0);
        }
    }

    public static void beforeRender(float tickDelta) {
        float skyBrightness = MinecraftClient.getInstance().world.getSkyBrightness(tickDelta);
        ColoredLightEntityRenderContext.setGlobal(skyBrightness);
    }

    public static void beforeRenderEntity(double x, double y, double z) {
        INSTANCE.read(x, y, z, ColoredLightEntityRenderContext::set);
    }

    public static void afterRenderEntity() {
        ColoredLightEntityRenderContext.end();
    }

    public static void read(double x, double y, double z, ColorConsumer consumer) {
        var readBlockPos = ColoredLightAdapter.readBlockPos.set(x, y, z);
        var chunk = ((BuiltChunkStorageAccess) MinecraftClient.getInstance().worldRenderer.chunks).getBuiltChunk(readBlockPos);
        if (chunk == null) {
            return;
        }

        var corners = ((ColoredLightBuiltChunk) chunk).getChunkLightColors();
        if (corners != null) {
            BlockPos origin = chunk.getOrigin();
            float localX = (float) (x - origin.getX()) / 16.0F;
            float localY = (float) (y - origin.getY()) / 16.0F;
            float localZ = (float) (z - origin.getZ()) / 16.0F;
            ColoredLightCorner.mix(corners, localX, localY, localZ, consumer);
        }
    }

    public static ChunkLightColorUpdater getChunkLightColorUpdater() {
        return chunkLightColorUpdater;
    }
}
