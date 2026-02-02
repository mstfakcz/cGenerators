# cGenerators

<div align="center">

![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.13+-brightgreen.svg)
![Java](https://img.shields.io/badge/java-8+-orange.svg)

**Professional Cobblestone Generator Upgrade System for Minecraft Skyblock**

</div>

## 📋 Overview

A highly customizable cobblestone generator upgrade system with multi-language support. Players upgrade through 5 tiers, each producing more valuable ores.

## ✨ Features

- 🎚️ **5-Tier System** - Progressive upgrades
- 🌍 **Multi-Language** - TR/EN built-in, extensible
- 💰 **Economy Integration** - Vault support
- 🎨 **Customizable GUI** - Separate config
- 📝 **Modular Configs** - Organized files
- 🔐 **Permission-Based** - LuckPerms compatible

## 🚀 Quick Start

### Requirements
- Spigot/Paper 1.13+
- Java 8+
- Vault
- Economy plugin

### Installation
```bash
1. Download latest release
2. Place JAR in plugins/
3. Start server
4. Configure files
5. Reload
```

## ⚙️ Configuration

**config.yml** - Main settings
```yaml
settings:
  language: 'tr'
  custom-command: '/jenerator'  # Your custom command!
tiers:
  1:
    cost: 0
    drops:
      COBBLESTONE: 70
```

**gui.yml** - Menu design
```yaml
menu:
  title: '&6&lGenerator'
  size: 27
```

**locale/*.yml** - Messages
```yaml
prefix: '&8[&6cGenerators&8]'
upgrade-success: '&aUpgraded!'
```

## 🎯 Commands

| Command | Description |
|---------|-------------|
| `/cgenerators` | Open menu |
| `/cgenadmin reload` | Reload configs |
| `/cgenadmin settier <player> <tier>` | Set tier |

## 🔑 Permissions

- `cgenerators.use` - Use menu
- `cgenerators.admin` - Admin commands
- `cgenerators.tier.1-5` - Tier access

## 🔧 Building

```bash
git clone https://github.com/yourusername/cGenerators.git
cd cGenerators
mvn clean package
```

## 📝 Tier System

| Tier | Cost | Key Drops |
|------|------|-----------|
| 1 | Free | Coal, Iron |
| 2 | 5K | + Gold |
| 3 | 15K | + Redstone |
| 4 | 50K | + Diamond |
| 5 | 150K | + Emerald |

## 🌐 Adding Languages

1. Copy `locale/tr.yml` → `locale/de.yml`
2. Translate messages
3. Set `language: 'de'`

## 📄 License

MIT License

## 💖 Support

- ⭐ Star this repo
- 🐛 Report issues
- 💡 Request features

---

<div align="center">Made for Minecraft Community</div>
