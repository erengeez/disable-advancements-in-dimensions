package erengee.daid.config;


import com.google.gson.*;
import erengee.daid.DisableAdvancementsInDimensions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModConfig {
    private static final File CONFIG_FILE = new File("config/disable_advancements_in_dimensions.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<String> blockedDimensions = new ArrayList<>();

    public static List<String> getBlockedDimensions() {
        return Collections.unmodifiableList(blockedDimensions);
    }

    private static final List<String> DEFAULT_DIMENSIONS = List.of(
            "example:dimension"
    );

    public static void init() {
        if (CONFIG_FILE.exists()) {
            load();
        } else {
            blockedDimensions = new ArrayList<>(DEFAULT_DIMENSIONS);
            save();
        }
    }

    public static void load() {
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);

            JsonArray dimensionsArray = json.getAsJsonArray("blockedDimensions");
            blockedDimensions = new ArrayList<>();
            dimensionsArray.forEach(element ->
                    blockedDimensions.add(element.getAsString()));
        } catch (IOException e) {
            DisableAdvancementsInDimensions.LOGGER.error("Failed to load config file", e);
            blockedDimensions = new ArrayList<>(DEFAULT_DIMENSIONS);
        }
    }

    public static void save() {
        JsonObject json = new JsonObject();

        JsonArray dimensionsArray = new JsonArray();
        blockedDimensions.forEach(dimensionsArray::add);
        json.add("blockedDimensions", dimensionsArray);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            DisableAdvancementsInDimensions.LOGGER.error("Failed to save config file", e);
        }
    }

    public static boolean isDimensionBlocked(String dimensionId) {
        return blockedDimensions.contains(dimensionId);
    }

    public static void addBlockedDimension(RegistryEntry.Reference<DimensionType> dimension) {
        String dimensionId = dimension.getIdAsString();
        if (!blockedDimensions.contains(dimensionId)) {
            blockedDimensions.add(dimensionId);
            save();
        }
    }

    public static void removeBlockedDimension(RegistryEntry.Reference<DimensionType> dimension) {
        String dimensionId = dimension.getIdAsString();
        blockedDimensions.remove(dimensionId);
        save();
    }
}
