package dev.gegy.colored_lights.mixin.shader.sodium;

import dev.gegy.colored_lights.resource.shader.PatchedShader;
import dev.gegy.colored_lights.resource.shader.PatchedUniform;
import dev.gegy.colored_lights.resource.shader.ShaderPatchManager;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.gl.shader.GlProgram;
import me.jellysquid.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = ShaderChunkRenderer.class, remap = false)
public class ShaderChunkRendererMixin {
    @Inject(method = "createShader", at = @At(value = "INVOKE", target = "me/jellysquid/mods/sodium/client/gl/shader/ShaderLoader.loadShader (Lme/jellysquid/mods/sodium/client/gl/shader/ShaderType;Lnet/minecraft/util/Identifier;Lme/jellysquid/mods/sodium/client/gl/shader/ShaderConstants;)Lme/jellysquid/mods/sodium/client/gl/shader/GlShader;"))
    private void initEarly(String path, ChunkShaderOptions options, CallbackInfoReturnable<GlProgram<ChunkShaderInterface>> cir) {
        ShaderPatchManager.startPatching(path);
    }

    @Inject(method = "end(Lme/jellysquid/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;)V", at = @At("RETURN"))
    private void initLate(TerrainRenderPass pass, CallbackInfo ci) {
        ShaderPatchManager.stopPatching();
    }
}
