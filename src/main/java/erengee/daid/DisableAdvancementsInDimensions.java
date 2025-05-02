package erengee.daid;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import erengee.daid.commands.DimensionSuggestionProvider;
import erengee.daid.commands.DimensionSuggestionsMode;
import erengee.daid.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;

public class DisableAdvancementsInDimensions implements ModInitializer {
    public static final String MOD_ID = "disable-advancements-in-dimensions";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private final Command<ServerCommandSource> disableAdvancementsCommand = context -> {
        RegistryEntry.Reference<DimensionType> dimension =
                RegistryEntryReferenceArgumentType.getRegistryEntry(context, "dimension", RegistryKeys.DIMENSION_TYPE);
        ModConfig.addBlockedDimension(dimension);
        context.getSource().sendFeedback(() -> Text.literal("Disabled advancements in %s".formatted(dimension.getIdAsString())), false);
        return 1;
    };

    private final Command<ServerCommandSource> enableAdvancementsCommand = context -> {
        RegistryEntry.Reference<DimensionType> dimension =
                RegistryEntryReferenceArgumentType.getRegistryEntry(context, "dimension", RegistryKeys.DIMENSION_TYPE);
        ModConfig.removeBlockedDimension(dimension);
        context.getSource().sendFeedback(() -> Text.literal("Enabled advancements in %s".formatted(dimension.getIdAsString())), false);
        return 1;
    };

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ModConfig.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("disable_advancements")
                    .then(CommandManager.argument(
                                    "dimension", RegistryEntryReferenceArgumentType.registryEntry(registryAccess,
                                            RegistryKeys.DIMENSION_TYPE)
                            )
                            .suggests(new DimensionSuggestionProvider(DimensionSuggestionsMode.UNBLOCKED_DIMENSIONS))
                            .requires(source -> source.hasPermissionLevel(4))
                            .executes(disableAdvancementsCommand)));
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("enable_advancements")
                    .then(CommandManager.argument(
                                    "dimension", RegistryEntryReferenceArgumentType.registryEntry(registryAccess,
                                            RegistryKeys.DIMENSION_TYPE)
                            )
                            .suggests(new DimensionSuggestionProvider(DimensionSuggestionsMode.BLOCKED_DIMENSIONS))
                            .requires(source -> source.hasPermissionLevel(4))
                            .executes(enableAdvancementsCommand)));
        });
        LOGGER.info("DisableAdvancementsInDimensions initialized");
    }
}