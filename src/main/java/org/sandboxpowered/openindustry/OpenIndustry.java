package org.sandboxpowered.openindustry;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.item.BaseBlockItem;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.resources.ResourceConstants;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.openindustry.machine.*;

import java.util.function.Supplier;

import static org.sandboxpowered.api.resources.ResourceConstants.*;

public class OpenIndustry implements Addon {
    public static MachineReference<MachineIronFurnace> IRON_FURNACE;
    public static MachineReference<MachineExtractor> EXTRACTOR;
    public static MachineReference<MachineCompressor> COMPRESSOR;
    public static MachineReference<MachineMacerator> MACERATOR;
    public static MachineReference<MachineMassFabricator> MASS_FABRICATOR;
    public static MachineReference<MachineRecycler> RECYCLER;
    public static MachineReference<MachineGenerator> GENERATOR;
    public static MachineReference<MachineGeothermalGenerator> GEOTHERMAL_GENERATOR;
    public static MachineReference<MachineElectricFurnace> ELECTRIC_FURNACE;

    public static <L extends BaseMachineLogic> MachineReference<L> registerMachine(Registrar registrar, String name, Supplier<L> logic) {
        return registerMachine(registrar, name, true, logic);
    }

    public static <L extends BaseMachineLogic> MachineReference<L> registerMachine(Registrar registrar, String name, boolean hasRotation, Supplier<L> logic) {
        MachineBlock<L> machine = new MachineBlock<>(Block.Settings.builder(Material.METAL).setStrength(5, 6).build(), hasRotation, logic);
        registrar.register(name, machine);
        BlockEntity.Type<L> type = BlockEntity.Type.of(logic, machine);
        registrar.register(name, type);
        BaseBlockItem item = new BaseBlockItem(machine, new Item.Settings());
        registrar.register(name, item);
        return new MachineReference<>(machine, type, item);
    }

    public static void registerSimpleItem(Registrar registrar, String name) {
        registrar.register(name, new BaseItem(new Item.Settings()));
    }

    @Override
    public void init(SandboxAPI api) {
        api.getLog().info("Loading Example Addon");
    }

    public static final ResourceMaterial COPPER = ResourceConstants.material("copper");
    public static final ResourceMaterial TIN = ResourceConstants.material("tin");
    public static final ResourceMaterial LEAD = ResourceConstants.material("lead");
    public static final ResourceMaterial BRONZE = ResourceConstants.material("bronze");
    public static final ResourceMaterial STEEL = ResourceConstants.material("steel");
    public static final ResourceMaterial SILVER = ResourceConstants.material("silver");
    public static final ResourceMaterial IRIDIUM = ResourceConstants.material("iridium");
    public static final ResourceType<Item> SMALL_DUST = ResourceConstants.type("small_dust");
    public static final ResourceType<Item> PLATE = ResourceConstants.type("plate");
    public static final ResourceType<Item> CASING = ResourceConstants.type("casing");
    public static final ResourceType<Item> DENSE_PLATE = ResourceConstants.type("dense_plate");

    public static final ResourceType<Item>[] ALL = new ResourceType[]{
            INGOT, DUST, SMALL_DUST, NUGGET, PLATE, DENSE_PLATE, GEM,CASING
    };
    public static final ResourceType<?>[] METAL = new ResourceType<?>[]{
            INGOT, DUST, SMALL_DUST, NUGGET, PLATE, DENSE_PLATE, CASING, ORE, BLOCK
    };

    @Override
    public void register(SandboxAPI api, Registrar registrar) {
        api.getLog().info("Registering Content");

        api.getLog().info("Registering Resources");
        registrar.useRegistrarService(ResourceService.class, service -> {
            service.add(COPPER, METAL);
            service.add(TIN, METAL);
            service.add(LEAD, METAL);
            service.add(BRONZE, METAL);
            service.add(STEEL, METAL);
            service.add(SILVER, METAL);
            service.add(GOLD, PLATE, DENSE_PLATE, DUST, SMALL_DUST,CASING);
            service.add(IRON, PLATE, DENSE_PLATE, DUST, SMALL_DUST,CASING);
            service.add(LAPIS, PLATE, DENSE_PLATE, DUST, SMALL_DUST);
            service.add(OBSIDIAN, DUST, PLATE, DENSE_PLATE, SMALL_DUST);
            service.add(COAL, DUST);
            service.add(CLAY, DUST);
        });
        api.getLog().info("Registering Materials");
        registerSimpleItem(registrar, "circuit");
        registerSimpleItem(registrar, "adv_circuit");
        registerSimpleItem(registrar, "alloy");
        registerSimpleItem(registrar, "carbon_fibre");
        registerSimpleItem(registrar, "carbon_mesh");
        registerSimpleItem(registrar, "carbon_plate");
        registerSimpleItem(registrar, "scrap");
        registerSimpleItem(registrar, "scrap_box");
        registerSimpleItem(registrar, "uu_matter");
        registerSimpleItem(registrar, "resin");
        registerSimpleItem(registrar, "rubber");

        api.getLog().info("Registering Machines");
        IRON_FURNACE = registerMachine(registrar, "iron_furnace", MachineIronFurnace::new);
        GENERATOR = registerMachine(registrar, "generator", MachineGenerator::new);
        GEOTHERMAL_GENERATOR = registerMachine(registrar, "geo_generator", MachineGeothermalGenerator::new);
        MACERATOR = registerMachine(registrar, "macerator", MachineMacerator::new);
        MASS_FABRICATOR = registerMachine(registrar, "mass_fabricator", MachineMassFabricator::new);
        RECYCLER = registerMachine(registrar, "recycler", MachineRecycler::new);
        EXTRACTOR = registerMachine(registrar, "extractor", MachineExtractor::new);
        COMPRESSOR = registerMachine(registrar, "compressor", MachineCompressor::new);
        ELECTRIC_FURNACE = registerMachine(registrar, "electric_furnace", MachineElectricFurnace::new);
        api.getLog().info("Finished Registration");
    }
}