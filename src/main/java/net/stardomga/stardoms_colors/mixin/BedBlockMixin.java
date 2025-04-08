package net.stardomga.stardoms_colors.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.block.entity.BedBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.Block.dropStack;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {

    @Inject(method = "onBreak", at = @At("HEAD"), cancellable = true)
    private void onlyDropIfBrokenByPlayer(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        if (!world.isClient && player != null) {
            BedPart part = state.get(BedBlock.PART);

            // Only act if this is the FOOT of the bed
            if (part == BedPart.FOOT) {
                BlockPos headPos = pos.offset(state.get(BedBlock.FACING));
                BlockState headState = world.getBlockState(headPos);

                // Check if this is your custom bed
                if (headState.getBlock() instanceof BedBlock && world.getBlockEntity(headPos) instanceof BedBlockEntity) {
                    if (!player.isCreative()) {
                        ItemStack bedStack = new ItemStack(ModBlocks.BED.asItem());
                        BedBlockEntity bedEntity = (BedBlockEntity) world.getBlockEntity(pos);
                        if (bedEntity != null) {
                            bedStack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(bedEntity.color));
                        }
                        dropStack(world, pos, bedStack);
                    }
                } else {
                    // Handle vanilla bed drops: manually drop the bed item only if broken by player
                    if (!player.isCreative()) {
                        BedBlock bedBlock = (BedBlock)(Object)this;
                        ItemStack vanillaBedStack = bedBlock.asItem().getDefaultStack();
                        dropStack(world, pos, vanillaBedStack);
                    }
                }

                // Remove both parts manually and cancel default logic
                world.removeBlock(pos, false);
                world.removeBlock(headPos, false);
                cir.cancel();
            }
        }
    }
}
