# Disable Advancements In Dimensions

A Minecraft Fabric mod that allows you to disable advancements and their tracking in specific dimensions. Perfect for isolating custom dimensions (e.g., creative worlds) from interfering with your survival world's progression.

This mod *should* disable only actual displayable advancements and *should not* interfere with functionality of datapacks that rely on advancements. If you encounter any compatibility problems, please open an issue [here](https://github.com/erengeez/disable-advancements-in-dimensions/issues).

## Requirements

1. **Fabric Mod Loader**
2. **Fabric API**

## Configuration

### Config File
Edit `config/disable_advancements_in_dimensions.json` to add/remove dimension IDs.
**Example configuration**:
```json
{
  "blockedDimensions": [
    "minecraft:the_nether",
    "custom_mod:creative_dimension"
  ]
}
```

## Commands

- **Disable advancements in a dimension**:
  ```
  /disable_advancements <dimensionId>
  ```
  Example: `/disable_advancements minecraft:the_end`

- **Re-enable advancements in a dimension**:
  ```
  /enable_advancements <dimensionId>
  ```
  Example: `/enable_advancements custom_mod:mining_world`

**Notes**:
- Requires operator permissions.
- Changes are saved to the config file immediately.
- Accepts only valid dimension IDs.

## License

This mod is licensed under the **MIT License**. Feel free to modify and redistribute it.