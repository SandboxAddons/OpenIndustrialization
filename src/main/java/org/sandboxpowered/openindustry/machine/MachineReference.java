package org.sandboxpowered.openindustry.machine;

import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.item.BaseBlockItem;

public class MachineReference<L extends BaseMachineLogic> {
    private final MachineBlock<L> machine;
    private final BlockEntity.Type<L> machineLogicType;
    private final BaseBlockItem machineItem;

    public MachineReference(MachineBlock<L> machine, BlockEntity.Type<L> machineLogicType, BaseBlockItem machineItem) {
        this.machine = machine;
        this.machineLogicType = machineLogicType;
        this.machineItem = machineItem;
    }

    public MachineBlock<L> getMachine() {
        return machine;
    }

    public BlockEntity.Type<L> getMachineLogicType() {
        return machineLogicType;
    }

    public BaseBlockItem getMachineItem() {
        return machineItem;
    }
}
