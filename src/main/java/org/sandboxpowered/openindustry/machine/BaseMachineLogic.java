package org.sandboxpowered.openindustry.machine;

import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.entity.BaseBlockEntity;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.component.Components;
import org.sandboxpowered.api.component.Inventory;
import org.sandboxpowered.api.component.inventory.DefaultInventory;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.Items;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.Mono;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.util.nbt.WritableCompoundTag;

public abstract class BaseMachineLogic extends BaseBlockEntity implements BlockEntity.Tickable {
    private final DefaultInventory input = new DefaultInventory(1);
    private final DefaultInventory output = new DefaultInventory(1);
    private final Mono<Inventory> inputMono = Mono.of(input);
    private final Mono<Inventory> outputMono = Mono.of(output);
    private int remaining;
    private BlockState cachedState;


    public BaseMachineLogic(MachineReference<?> reference) {
        super(reference.getMachineLogicType());
    }

    public BlockState getCachedState() {
        if (cachedState == null)
            cachedState = getWorld().getBlockState(getPosition());
        return cachedState;
    }

    public void updateState() {
        cachedState = null;
    }

    @Override
    public void read(ReadableCompoundTag tag) {
        CompoundTag inputTag = tag.getCompoundTag("input");
        CompoundTag outputTag = tag.getCompoundTag("output");
        input.read(inputTag);
        output.read(outputTag);
        remaining = tag.getInt("remaining");
    }

    @Override
    public void write(WritableCompoundTag tag) {
        CompoundTag inputTag = CompoundTag.create();
        CompoundTag outputTag = CompoundTag.create();
        input.write(inputTag);
        output.write(outputTag);
        tag.setTag("input", inputTag);
        tag.setTag("output", outputTag);
        tag.setInt("remaining", remaining);
    }

    @Override
    public <X> Mono<X> getComponent(Component<X> component, @Nullable Direction side) {
        if (side != null && component == Components.INVENTORY_COMPONENT) {
            if (side == Direction.UP)
                return inputMono.cast();
            if (side == Direction.DOWN)
                return outputMono.cast();
        }
        return super.getComponent(component, side);
    }

    @Override
    public void onTick() {
        ItemStack in = getInput();
        if (!in.isEmpty()) {
            Item recipeOut = Items.IRON_INGOT.get();
            boolean active = remaining > -1;
            if (remaining == 0) {
                ItemStack out = getOutput();
                in.shrink();
                if (!out.isEmpty()) {
                    out.grow();
                } else {
                    output.setStack(0, ItemStack.of(recipeOut));
                }
                remaining = -1;
                active = false;
            } else {
                if (remaining != -1)
                    remaining--;
            }
            if (remaining == -1) {
                ItemStack out = getOutput();
                if (!out.isEmpty() && (out.getItem() != recipeOut || out.getCount() >= output.getMaxStackSize(0))) {
                    remaining = -1;
                    active = false;
                } else {
                    remaining = 20;
                    active = true;
                }
            }
            if (getCachedState().get(MachineBlock.ACTIVE) != active) {
                getWorld().setBlockState(getPosition(), getCachedState().with(MachineBlock.ACTIVE, active));
                updateState();
            }
        } else {
            if (remaining != -1) {
                remaining = -1;
                if (getCachedState().get(MachineBlock.ACTIVE)) {
                    getWorld().setBlockState(getPosition(), getCachedState().with(MachineBlock.ACTIVE, false));
                    updateState();
                }
            }
        }
    }

    public ItemStack getInput() {
        return input.get(0);
    }

    public ItemStack getOutput() {
        return output.get(0);
    }

    public InteractionResult onInteract(BlockState state, PlayerEntity player, Hand hand, Direction side, Vec3f hit) {
        return InteractionResult.IGNORE;
    }
}