package net.auricmc.weapons;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.Map;
import java.util.Set;

import net.minecraft.registry.tag.BlockTags;

public class WeaponsMod implements ModInitializer {
    public static final String MOD_ID = "auricweapons";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Item WOODEN_DAGGER, STONE_DAGGER, IRON_DAGGER, GOLDEN_DAGGER, DIAMOND_DAGGER, NETHERITE_DAGGER;
    public static Item WOODEN_DAXE, STONE_DAXE, IRON_DAXE, GOLDEN_DAXE, DIAMOND_DAXE, NETHERITE_DAXE;
    public static Item WOODEN_SCYTHE, STONE_SCYTHE, IRON_SCYTHE, GOLDEN_SCYTHE, DIAMOND_SCYTHE, NETHERITE_SCYTHE;

    // Axe mapping: log -> stripped log
    private static final Map<Block, Block> STRIPPABLE_LOGS = Map.of(
            Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG,
            Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG,
            Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG,
            Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG,
            Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG,
            Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG);

    // Optional: scythe crops
    private static final Set<Block> CROP_BLOCKS = Set.of(
            Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS);

    @Override
    public void onInitialize() {
        WOODEN_DAGGER = RegisterWeapon("wooden_dagger", 30, 3.0f, -1.6f, WeaponType.DAGGER);
        STONE_DAGGER = RegisterWeapon("stone_dagger", 75, 4.0f, -1.6f, WeaponType.DAGGER);
        IRON_DAGGER = RegisterWeapon("iron_dagger", 120, 5.0f, -1.6f, WeaponType.DAGGER);
        GOLDEN_DAGGER = RegisterWeapon("golden_dagger", 16, 3.0f, -1.4f, WeaponType.DAGGER);
        DIAMOND_DAGGER = RegisterWeapon("diamond_dagger", 780, 6.0f, -1.6f, WeaponType.DAGGER);
        NETHERITE_DAGGER = RegisterWeapon("netherite_dagger", 1016, 7.0f, -1.6f, WeaponType.DAGGER);

        WOODEN_DAXE = RegisterWeapon("wooden_daxe", 120, 7.0f, -2.8f, WeaponType.DAXE);
        STONE_DAXE = RegisterWeapon("stone_daxe", 260, 8.0f, -2.8f, WeaponType.DAXE);
        IRON_DAXE = RegisterWeapon("iron_daxe", 500, 9.0f, -2.8f, WeaponType.DAXE);
        GOLDEN_DAXE = RegisterWeapon("golden_daxe", 64, 7.1f, -2.6f, WeaponType.DAXE);
        DIAMOND_DAXE = RegisterWeapon("diamond_daxe", 3122, 10.0f, -2.8f, WeaponType.DAXE);
        NETHERITE_DAXE = RegisterWeapon("netherite_daxe", 4062, 11.0f, -2.8f, WeaponType.DAXE);

        WOODEN_SCYTHE = RegisterWeapon("wooden_scythe", 90, 9.0f, -2.0f, WeaponType.SCYTHE);
        STONE_SCYTHE = RegisterWeapon("stone_scythe", 200, 10.0f, -2.0f, WeaponType.SCYTHE);
        IRON_SCYTHE = RegisterWeapon("iron_scythe", 375, 11.0f, -2.0f, WeaponType.SCYTHE);
        GOLDEN_SCYTHE = RegisterWeapon("golden_scythe", 48, 9.1f, -1.8f, WeaponType.SCYTHE);
        DIAMOND_SCYTHE = RegisterWeapon("diamond_scythe", 2310, 12.0f, -2.0f, WeaponType.SCYTHE);
        NETHERITE_SCYTHE = RegisterWeapon("netherite_scythe", 3045, 13.0f, -2.0f, WeaponType.SCYTHE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(WOODEN_DAGGER);
            entries.add(STONE_DAGGER);
            entries.add(IRON_DAGGER);
            entries.add(GOLDEN_DAGGER);
            entries.add(DIAMOND_DAGGER);
            entries.add(NETHERITE_DAGGER);
            entries.add(WOODEN_DAXE);
            entries.add(STONE_DAXE);
            entries.add(IRON_DAXE);
            entries.add(GOLDEN_DAXE);
            entries.add(DIAMOND_DAXE);
            entries.add(NETHERITE_DAXE);
            entries.add(WOODEN_SCYTHE);
            entries.add(STONE_SCYTHE);
            entries.add(IRON_SCYTHE);
            entries.add(GOLDEN_SCYTHE);
            entries.add(DIAMOND_SCYTHE);
            entries.add(NETHERITE_SCYTHE);
        });

        LOGGER.info("Auric Weapons initialized!");
    }

    private static Item RegisterWeapon(String name, int durability, float attackDamage, float attackSpeed,
            WeaponType type) {
        Identifier id = Identifier.of(MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = new Item.Settings().maxCount(1).maxDamage(durability).registryKey(key);
        return Registry.register(Registries.ITEM, key, new WeaponItem(settings, attackDamage, attackSpeed, type));
    }

    public enum WeaponType {
        DAGGER, DAXE, SCYTHE
    }

    public static class WeaponItem extends Item {
        private final float attackDamage, attackSpeed;
        private final WeaponType weaponType;

        public WeaponItem(Settings settings, float attackDamage, float attackSpeed, WeaponType weaponType) {
            super(settings);
            this.attackDamage = attackDamage;
            this.attackSpeed = attackSpeed;
            this.weaponType = weaponType;
        }

        @Override
        public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
            if (!attacker.getWorld().isClient && target.isAlive()) {
                float extraDamage = attackDamage - 1.0f;
                if (extraDamage > 0)
                    target.setHealth(Math.max(0, target.getHealth() - extraDamage));
            }
            stack.damage(1, attacker, LivingEntity.getSlotForHand(attacker.getActiveHand()));
        }

        @Override
        public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
            if (!world.isClient && state.getHardness(world, pos) != 0) {
                stack.damage(1, miner, LivingEntity.getSlotForHand(miner.getActiveHand()));
            }
            return true;
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack stack = context.getStack();

            if (weaponType == WeaponType.DAXE) {
                Block stripped = STRIPPABLE_LOGS.get(state.getBlock());
                if (stripped != null) {
                    world.setBlockState(pos,
                            stripped.getDefaultState().with(Properties.AXIS, state.get(Properties.AXIS)), 3);
                    stack.damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
                    return ActionResult.SUCCESS;
                }
            }

            if (weaponType == WeaponType.SCYTHE) {
                Block block = state.getBlock();
                if (block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.GRASS_BLOCK || 
                    block == Blocks.ROOTED_DIRT || block == Blocks.DIRT_PATH || block == Blocks.PODZOL ||
                    block == Blocks.MYCELIUM || block == Blocks.MUD) {
                    if (!world.isClient) {
                        world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 3);
                        stack.damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
                    }
                    return ActionResult.SUCCESS;
                } else if (CROP_BLOCKS.contains(block)) {
                    if (!world.isClient) {
                        world.breakBlock(pos, true);
                        stack.damage(1, context.getPlayer(), LivingEntity.getSlotForHand(context.getHand()));
                    }
                    return ActionResult.SUCCESS;
                }
            }

            return super.useOnBlock(context);
        }

        @Override
        public float getMiningSpeed(ItemStack stack, BlockState state) {
            if (weaponType == WeaponType.DAXE && isEffectiveOn(state))
                return getAxeMiningSpeed();
            if (weaponType == WeaponType.SCYTHE && isEffectiveOn(state))
                return getHoeMiningSpeed();
            return 1.0f;
        }

        public boolean isEffectiveOn(BlockState state) {
            if (weaponType == WeaponType.DAXE)
                return state.isIn(BlockTags.AXE_MINEABLE) || STRIPPABLE_LOGS.containsKey(state.getBlock());
            if (weaponType == WeaponType.SCYTHE)
                return state.isIn(BlockTags.HOE_MINEABLE) || state.isIn(BlockTags.CROPS)
                        || state.isIn(BlockTags.LEAVES);
            return false;
        }

        private float getAxeMiningSpeed() {
            if (attackDamage == 7.0f)
                return 1.5f;
            if (attackDamage == 8.0f)
                return 3.5f;
            if (attackDamage == 9.0f)
                return 5.5f;
            if (attackDamage == 10.0f)
                return 7.5f;
            if (attackDamage == 11.0f)
                return 8.5f;
            return 11.5f;
        }

        private float getHoeMiningSpeed() {
            if (attackDamage == 9.0f)
                return 1.0f;
            if (attackDamage == 10.0f)
                return 3.0f;
            if (attackDamage == 11.0f)
                return 5.0f;
            if (attackDamage == 12.0f)
                return 7.0f;
            if (attackDamage == 13.0f)
                return 8.0f;
            return 11.0f;
        }

        public float getAttackDamage() {
            return attackDamage;
        }

        public float getAttackSpeed() {
            return attackSpeed;
        }

        public WeaponType getWeaponType() {
            return weaponType;
        }

        public boolean isEnchantable(ItemStack stack) {
            return true; // allows anvil/table
        }

        public int getEnchantability() {
            switch (weaponType) {
                case DAGGER:
                    return 10;
                case DAXE:
                    return 15;
                case SCYTHE:
                    return 12;
            }
            return 1;
        }

        public boolean canRepair(ItemStack stack, ItemStack ingredient) {
            // Allow repairing with appropriate materials
            switch (weaponType) {
                case DAGGER:
                case DAXE:
                case SCYTHE:
                    // Check attack damage to determine material tier
                    if (attackDamage <= 4.0f) return ingredient.isOf(net.minecraft.item.Items.STICK); // Wood
                    if (attackDamage <= 5.0f) return ingredient.isOf(net.minecraft.item.Items.COBBLESTONE); // Stone
                    if (attackDamage <= 6.0f) return ingredient.isOf(net.minecraft.item.Items.IRON_INGOT); // Iron
                    if (attackDamage <= 7.0f) return ingredient.isOf(net.minecraft.item.Items.GOLD_INGOT); // Gold
                    if (attackDamage <= 12.0f) return ingredient.isOf(net.minecraft.item.Items.DIAMOND); // Diamond
                    return ingredient.isOf(net.minecraft.item.Items.NETHERITE_INGOT); // Netherite
            }
            return false;
        }


    }
}