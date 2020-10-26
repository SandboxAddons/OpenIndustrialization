package org.sandboxpowered.openindustry.machine;

import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.Properties;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.state.property.BooleanProperty;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;

import java.util.function.Supplier;

public class MachineBlock<L extends BaseMachineLogic> extends BaseBlock {
    public static final Property<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final Property<Boolean> ACTIVE = BooleanProperty.of("active");

    private final boolean hasRotation;
    private final Supplier<L> logicSupplier;

    public MachineBlock(Settings settings, boolean hasRotation, Supplier<L> logicSupplier) {
        super(settings);
        this.hasRotation = hasRotation;
        this.logicSupplier = logicSupplier;
    }

    @Override
    protected BlockState createBaseState(BlockState baseState) {
        if (!hasRotation)
            return baseState.with(ACTIVE, false);
        return baseState.with(FACING, Direction.NORTH).with(ACTIVE, false);
    }

    @Override
    public void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (hasRotation)
            builder.add(FACING);
        builder.add(ACTIVE);
    }

    @Override
    public boolean hasBlockEntity() {
        return true;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(WorldReader reader) {
        return logicSupplier.get();
    }

    public L getLogic(WorldReader reader, Position position) {
        return (L) reader.getBlockEntity(position);
    }

    @Override
    public BlockState getStateForPlacement(WorldReader reader, Position pos, PlayerEntity player, Hand hand, ItemStack stack, Direction side, Vec3d hitPos) {
        if (hasRotation)
            return getBaseState().with(FACING, player.getHorizontalFacing().getOppositeDirection());
        return getBaseState();
    }

    @Override
    public InteractionResult onBlockUsed(World world, Position pos, BlockState state, PlayerEntity player, Hand hand, Direction side, Vec3f hit) {
        return getLogic(world, pos).onInteract(state, player, hand, side, hit);
    }
}