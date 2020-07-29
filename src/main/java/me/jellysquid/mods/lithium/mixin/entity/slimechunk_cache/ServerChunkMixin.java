package me.jellysquid.mods.lithium.mixin.entity.slimechunk_cache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.jellysquid.mods.lithium.common.chunk.SlimeChunkStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.poi.PointOfInterestStorage;

@Mixin(ChunkSerializer.class)
public class ServerChunkMixin {
    @Inject(at = @At("RETURN"), method = "deserialize")
    private static void cacheSlimeChunk(ServerWorld world, StructureManager structureManager, PointOfInterestStorage poiStorage, ChunkPos pos, CompoundTag tag, CallbackInfo ci) {
        CompoundTag compoundTag = tag.getCompound("Level");
        boolean isSlime = compoundTag.getBoolean("slimeChunk-LITHIUM");

        if(isSlime) {
            SlimeChunkStorage.addSlimeChunk(pos);
        }
    }

    @Inject(at = @At("RETURN"), method = "serialize", cancellable = true)
    private static void attachSlimeChunk(ServerWorld world, Chunk chunk, CallbackInfoReturnable<CompoundTag> cir) {
        cir.getReturnValue().putBoolean("slimeChunk-LITHIUM", SlimeChunkStorage.checkSlimeChunk(chunk.getPos()));
    }
}o