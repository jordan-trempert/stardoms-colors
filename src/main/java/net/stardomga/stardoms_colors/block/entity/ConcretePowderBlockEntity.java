package net.stardomga.stardoms_colors.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stardomga.stardoms_colors.block.ConcretePowderBlock;
import net.stardomga.stardoms_colors.block.ModBlocks;
import net.stardomga.stardoms_colors.util.BlockEntityDataCache;
import org.jetbrains.annotations.Nullable;

import static net.stardomga.stardoms_colors.block.ModBlocks.CONCRETE_POWDER;

public class ConcretePowderBlockEntity extends BlockEntity {
    public int color = 0xFFFFFF;


    public ConcretePowderBlockEntity(BlockPos pos, BlockState state) {
        super(StardomsColorsBlockEntityTypes.CONCRETE_POWDER, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("color", this.color);
        nbt.putInt("minecraft:dyed_color", this.color);
    }



    public void Solidify(BlockPos pos){
        world.setBlockState(pos, ModBlocks.CONCRETE.getDefaultState());
    }





    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.color = nbt.getInt("color", 0x008080);

    }

    public ItemStack getColoredDrop() {
        ItemStack stack = new ItemStack(CONCRETE_POWDER.asItem());

        // Create components container
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(
                this.color // Your block entity's color
        ));

        return stack;
    }




    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        if (this.color != color) {
            this.color = color;
            markDirty();

            if (world != null) {
                if (!world.isClient) {
                    // Server-side update
                    world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                    sendUpdatePacket();
                } else {
                    // Client-side immediate visual update
                    world.updateListeners(pos, getCachedState(), getCachedState(), 0);
                }
            }
        }
    }

    private void sendUpdatePacket() {
        if (world instanceof ServerWorld serverWorld) {
            Packet<ClientPlayPacketListener> packet = this.toUpdatePacket();
            if (packet != null) {
                BlockPos.stream(pos.add(-64, -64, -64), pos.add(64, 64, 64))
                        .forEach(blockPos -> {
                            if (world.getBlockEntity(blockPos) == this) {
                                serverWorld.getChunkManager().markForUpdate(blockPos);
                            }
                        });

                // Send to nearby players
                serverWorld.getPlayers().stream()
                        .filter(p -> p.getBlockPos().isWithinDistance(pos, 64))
                        .forEach(p -> p.networkHandler.sendPacket(packet));
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    // Optional: Implement if you want to use the render data optimization
    @Override
    public @Nullable Object getRenderData() {
        return color;
    }
}