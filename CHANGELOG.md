# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-02-02

### Added
- 🌍 Multi-language support system with language files
- 📁 Separate `gui.yml` configuration file for menu customization
- 📁 Separate `locale/` folder for language files
- 🔧 `LanguageManager` for handling translations
- 🔧 `GUIManager` for handling GUI configurations
- 🎨 `MenuHolder` class for reliable menu detection
- 🐛 Debug mode with detailed logging
- 📚 Language files: Turkish (`tr.yml`) and English (`en.yml`)
- ⚙️ Configurable language selection in `config.yml`
- 📖 Comprehensive GitHub README with badges and documentation
- 📝 CHANGELOG.md for tracking version history
- 🔒 .gitignore for proper Git management

### Changed
- 🔄 Renamed plugin from `SimpleCobbleGen` to `cGenerators`
- 🔄 Package structure from `com.skyblock.cobblegen` to `com.skyblock.cgenerators`
- 🔄 Main class from `SimpleCobbleGen.java` to `CGenerators.java`
- 🔄 Commands from `/cobblegen` to `/cgenerators` (with aliases)
- 🔄 Permissions from `cobblegen.*` to `cgenerators.*`
- 📦 Modularized configuration files (split into config.yml, gui.yml, locale/)
- 🎨 Improved code architecture with manager-based system
- 📚 Updated all documentation to reflect new structure

### Fixed
- 🐛 Menu item duplication bug where players could take items from the menu
- 🐛 Event cancellation issues in inventory click handler
- 🔧 MenuListener now uses InventoryHolder pattern for reliable detection
- 🔧 Improved menu title matching with proper color code handling

### Removed
- ❌ PlaceholderAPI dependency (will be re-added as optional in future)
- ❌ Inline messages from config.yml (moved to locale files)
- ❌ GUI settings from config.yml (moved to gui.yml)

## [1.0.0] - 2024-02-01

### Added
- 🎉 Initial release
- ⚡ 5-tier cobblestone generator upgrade system
- 🎨 GUI menu for tier upgrades
- 💰 Vault economy integration
- 🔐 Permission-based tier system
- 🎵 Sound effects for interactions
- ⚙️ Fully configurable tiers and drop rates
- 📊 Tier-based ore generation (Cobblestone, Coal, Iron, Gold, Redstone, Diamond, Lapis, Emerald)
- 👥 Admin commands for tier management
- 🔄 Hot reload support
- 📝 Turkish language support (hardcoded)

### Technical
- Built with Spigot API 1.20.1
- Java 8 compatibility
- Maven build system
- Vault API integration
- LuckPerms compatible

---

## Version History

| Version | Release Date | Highlights |
|---------|-------------|------------|
| [2.0.0](#200---2024-02-02) | 2024-02-02 | Multi-language, modular configs, bug fixes |
| [1.0.0](#100---2024-02-01) | 2024-02-01 | Initial release |

---

## Upgrade Guide

### From 1.0.0 to 2.0.0

**⚠️ BREAKING CHANGES - Manual migration required**

1. **Backup your current configuration**
   ```bash
   cp -r plugins/SimpleCobbleGen plugins/SimpleCobbleGen.backup
   ```

2. **Stop the server**

3. **Remove old plugin**
   ```bash
   rm plugins/SimpleCobbleGen-*.jar
   rm -rf plugins/SimpleCobbleGen
   ```

4. **Install new plugin**
   - Place `cGenerators-2.0.0.jar` in `plugins/`
   - Start server to generate new configs

5. **Migrate settings**
   - Copy tier configurations from old `config.yml` to new `config.yml`
   - Copy custom messages to `locale/tr.yml` or `locale/en.yml`
   - Update permissions:
     ```
     OLD: cobblegen.tier.1
     NEW: cgenerators.tier.1
     ```

6. **Update permissions in LuckPerms**
   ```bash
   /lp group default permission unset cobblegen.tier.1
   /lp group default permission set cgenerators.tier.1
   ```

---

## Future Plans

### Version 2.1.0 (Planned)
- [ ] PlaceholderAPI support
- [ ] More language files (German, French, Spanish)
- [ ] Tier progression sound customization
- [ ] Particle effects for tier upgrades

### Version 2.2.0 (Planned)
- [ ] MySQL database support for tier data
- [ ] Advanced statistics tracking
- [ ] Leaderboard system
- [ ] Generator cooldown system

### Version 3.0.0 (Planned)
- [ ] Custom generator types (not just cobblestone)
- [ ] Generator presets system
- [ ] Web panel integration
- [ ] Advanced API for developers

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute to this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
