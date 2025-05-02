package erengee.daid.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import erengee.daid.config.ModConfig;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class DimensionSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

    private final DimensionSuggestionsMode mode;


    public DimensionSuggestionProvider(DimensionSuggestionsMode mode) {
        this.mode = mode;
    }

    public DimensionSuggestionProvider() {
        this(DimensionSuggestionsMode.ALL_DIMENSIONS);
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context,
                                                         SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        Registry<DimensionType> dimensionsRegistry = source.getRegistryManager().get(RegistryKeys.DIMENSION_TYPE);

        Stream<Identifier> ids = dimensionsRegistry.getIds().stream().filter(id -> switch (mode) {
            case ALL_DIMENSIONS -> true;
            case BLOCKED_DIMENSIONS -> ModConfig.isDimensionBlocked(id.toString());
            case UNBLOCKED_DIMENSIONS -> !ModConfig.isDimensionBlocked(id.toString());
        });

        ids.forEach(id -> builder.suggest(id.toString()));

        return builder.buildFuture();
    }
}
