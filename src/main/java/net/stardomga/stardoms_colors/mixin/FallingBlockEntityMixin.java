package net.stardomga.stardoms_colors.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.entity.ConcretePowderBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @WrapOperation(
            method = "spawnFromBlock",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/entity/FallingBlockEntity"
            )
    )
    private static FallingBlockEntity wrapFallingBlockEntityConstructor(
            World world, double x, double y, double z, BlockState state,
            Operation<FallingBlockEntity> original,
            World worldArg, BlockPos pos, BlockState originalState
    ) {
        FallingBlockEntity entity = original.call(world, x, y, z, state);

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ConcretePowderBlockEntity coloredBE) {
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("color", coloredBE.color);
            entity.blockEntityData = nbt;
        }

        return entity;
    }
}