package net.stardomga.stardoms_colors.block.entity;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
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
import net.minecraft.util.math.Direction;
import net.stardomga.stardoms_colors.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import static net.stardomga.stardoms_colors.block.ModBlocks.BED;
import static net.stardomga.stardoms_colors.block.ModBlocks.BRICKS;

public class BedBlockEntity extends BlockEntity {
    public int color = 0xFFFFFF;

    public BedBlockEntity(BlockPos pos, BlockState state) {
        super(StardomsColorsBlockEntityTypes.BED, pos, state);
    }

    public void syncImmediately() {
        if (world != null && !world.isClient) {
            // Update self
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

            // Update other part
            BlockPos otherPos = getOtherPartPos();
            if (otherPos != null && world.getBlockEntity(otherPos) instanceof BedBlockEntity other) {
                other.color = this.color;
                other.markDirty();
                world.updateListeners(otherPos, world.getBlockState(otherPos), world.getBlockState(otherPos), Block.NOTIFY_ALL);
            }

            // Send packets
            sendUpdatePacket();
        }
    }

    public void forceImmediateUpdate() {
        if (world != null && !world.isClient) {
            // Update self
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

            // Update other part
            syncColorToOtherPart();

            // Send network packets
            sendUpdatePacket();
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putInt("color", this.color);
        nbt.putInt("minecraft:dyed_color", this.color);
    }

    public boolean isHead() {
        if (world == null) return false;
        BlockState state = getCachedState();
        return state.contains(BedBlock.PART) &&
                state.get(BedBlock.PART) == BedPart.HEAD;
    }

    public boolean isFoot() {
        if (world == null) return false;
        BlockState state = getCachedState();
        return state.contains(BedBlock.PART) &&
                state.get(BedBlock.PART) == BedPart.FOOT;
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);

        this.color = nbt.getInt("color", 0x008080);
        syncColorToOtherPart(); // Sync color when loading
    }



    public ItemStack getColoredDrop() {
        ItemStack stack = new ItemStack(BED.asItem());
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(this.color));
        return stack;
    }



    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        if (this.color != color) {
            this.color = color;
            markDirty();
            syncColorToOtherPart(); // Sync to other part when color changes

            if (world != null) {
                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
                sendUpdatePacket();
            }
        }
    }

    /**
     * Gets the position of the other part of the bed
     */
    @Nullable
    public BlockPos getOtherPartPos() {
        if (world == null || !hasWorld()) return null;

        BlockState state = getCachedState();
        if (!state.contains(BedBlock.PART)) return null;

        Direction facing = state.get(BedBlock.FACING);
        return isHead() ?
                pos.offset(facing.getOpposite()) : // Get foot position
                pos.offset(facing); // Get head position
    }

    /**
     * Syncs the current color to the other part of the bed
     */
    public void syncColorToOtherPart() {
        BlockPos otherPos = getOtherPartPos();
        if (otherPos != null && world != null && !world.isClient) {
            if (world.getBlockEntity(otherPos) instanceof BedBlockEntity other) {
                // Set color without triggering another sync to avoid loops
                other.color = this.color;
                other.markDirty();

                // Force immediate client update
                world.updateListeners(otherPos, world.getBlockState(otherPos),
                        world.getBlockState(otherPos), Block.NOTIFY_ALL);

                // Send network update
                other.sendUpdatePacket();
            }
        }
    }



    public void initializeColor(){
        syncColorToOtherPart();
    }

    public void updateColorAndSync(int newColor) {
        if (this.color != newColor) {
            this.color = newColor;
            this.markDirty();

            // Force immediate visual update
            if (world != null) {
                // Update self
                world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);

                // Update other part
                BlockPos otherPos = getOtherPartPos();
                if (otherPos != null && world.getBlockEntity(otherPos) instanceof BedBlockEntity other) {
                    other.color = newColor;
                    other.markDirty();
                    world.updateListeners(otherPos, world.getBlockState(otherPos),
                            world.getBlockState(otherPos), Block.NOTIFY_ALL);
                }

                // Send network updates
                sendImmediateNetworkUpdate();
            }
        }
    }

    public void atomicColorUpdate(int newColor) {
        if (this.color == newColor) return;

        this.color = newColor;
        this.markDirty();

        if (world != null && !world.isClient) {
            // Force update both parts
            forceUpdateAt(pos);

            BlockPos otherPos = getOtherPartPos();
            if (otherPos != null) {
                forceUpdateAt(otherPos);
            }

            // Nuclear network sync
            sendNuclearUpdate();
        }
    }

    private void forceUpdateAt(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
        world.scheduleBlockTick(pos, BED, 0);
    }

    private void sendNuclearUpdate() {
        if (world instanceof ServerWorld serverWorld) {
            // Create packets for both parts
            Packet<ClientPlayPacketListener> selfPacket = this.toUpdatePacket();
            Packet<ClientPlayPacketListener> otherPacket = null;

            BlockPos otherPos = getOtherPartPos();
            if (otherPos != null && world.getBlockEntity(otherPos) instanceof BedBlockEntity other) {
                otherPacket = other.toUpdatePacket();
            }



            // Force chunk updates
            serverWorld.getChunkManager().markForUpdate(pos);
            if (otherPos != null) {
                serverWorld.getChunkManager().markForUpdate(otherPos);
            }
        }
    }


    private void sendImmediateNetworkUpdate() {
        if (world instanceof ServerWorld serverWorld) {
            // Create update packet
            Packet<ClientPlayPacketListener> packet = this.toUpdatePacket();

            // Send to tracking players
            serverWorld.getChunkManager().markForUpdate(pos);

            // Send to other part
            BlockPos otherPos = getOtherPartPos();
            if (otherPos != null) {
                serverWorld.getChunkManager().markForUpdate(otherPos);
            }

            // Send to nearby players
            serverWorld.getPlayers().stream()
                    .filter(p -> p.getBlockPos().isWithinDistance(pos, 64))
                    .forEach(p -> {
                        p.networkHandler.sendPacket(packet);
                        if (otherPos != null) {
                            BlockEntity otherBe = world.getBlockEntity(otherPos);
                            if (otherBe != null) {
                                p.networkHandler.sendPacket(otherBe.toUpdatePacket());
                            }
                        }
                    });
        }
    }

    private void sendUpdatePacket() {
        if (world instanceof ServerWorld serverWorld) {
            Packet<ClientPlayPacketListener> packet = toUpdatePacket();
            if (packet != null) {
                // Send to tracking players
                serverWorld.getChunkManager().markForUpdate(pos);
                if (getOtherPartPos() != null) {
                    serverWorld.getChunkManager().markForUpdate(getOtherPartPos());
                }

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

    @Override
    public @Nullable Object getRenderData() {
        return color;
    }
}