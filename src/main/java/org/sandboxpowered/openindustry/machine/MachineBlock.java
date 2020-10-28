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
import org.sandboxpowered.openindustry.RotationType;

import java.util.function.Supplier;

public class MachineBlock<L extends BaseMachineLogic> extends BaseBlock {
    public static final Property<Boolean> ACTIVE = BooleanProperty.of("active");

    private final RotationType rotationType;
    private final Supplier<L> logicSupplier;

    public MachineBlock(Settings settings, RotationType rotationType, Supplier<L> logicSupplier) {
        super(settings);
        this.rotationType = rotationType;
        this.logicSupplier = logicSupplier;
    }

    @Override
    protected BlockState createBaseState(BlockState baseState) {
        if (rotationType == RotationType.NONE)
            return baseState.with(ACTIVE, false);
        return baseState.with(rotationType.getProperty(), rotationType.getDefaultValue()).with(ACTIVE, false);
    }

    @Override
    public void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (rotationType != RotationType.NONE)
            builder.add(rotationType.getProperty());
        builder.add(ACTIVE);
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
        if (rotationType != RotationType.NONE)
            return getBaseState().with(rotationType.getProperty(), player.getHorizontalFacing().getOppositeDirection());
        return getBaseState();
    }

    @Override
    public InteractionResult onBlockUsed(World world, Position pos, BlockState state, PlayerEntity player, Hand hand, Direction side, Vec3f hit) {
        return getLogic(world, pos).onInteract(state, player, hand, side, hit);
    }
}