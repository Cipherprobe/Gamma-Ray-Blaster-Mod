package net.narwhal1226.rayblaster;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.rmi.registry.Registry;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GammaRayBlasterMod.MOD_ID)
public class GammaRayBlasterMod
{
    public static final String MOD_ID = "rayblaster";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GammaRayBlasterMod.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GammaRayBlasterMod.MOD_ID);

    public static final RegistryObject<Item> NUCLEGON = ITEMS.register("nuclegon",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_NUCLEGON = ITEMS.register("raw_nuclegon",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXCITED_URANIUM = ITEMS.register("excited_uranium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Block> NUCLEGON_ORE = registerBlock("nuclegon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f)
            .requiresCorrectToolForDrops(), UniformInt.of(6, 9)));

    public static final RegistryObject<Block> URANIUM_ORE = registerBlock("uranium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f)
            .requiresCorrectToolForDrops(), UniformInt.of(9, 11)));

    public static final RegistryObject<Block> DEEPSLATE_NUCLEGON_ORE = registerBlock("deepslate_nuclegon_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f)
            .requiresCorrectToolForDrops(), UniformInt.of(6, 9)));

    public static final RegistryObject<Block> DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE).strength(6f)
            .requiresCorrectToolForDrops(), UniformInt.of(9, 11)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static  <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public GammaRayBlasterMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::buildContents);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "gammaraytab"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("item_group." + MOD_ID + ".rayblaster"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(NUCLEGON.get()))
                        // Add default items to tab
                        .displayItems((enabledFlags, populator, hasPermissions) -> {
                            populator.accept(NUCLEGON.get());
                            populator.accept(RAW_NUCLEGON.get());
                            populator.accept(EXCITED_URANIUM.get());
                            populator.accept(RAW_URANIUM.get());
                            populator.accept(NUCLEGON_ORE.get());
                            populator.accept(DEEPSLATE_NUCLEGON_ORE.get());
                            populator.accept(URANIUM_ORE.get());
                            populator.accept(DEEPSLATE_URANIUM_ORE.get());
                        })
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
