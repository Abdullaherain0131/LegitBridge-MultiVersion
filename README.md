<h1 align="center">LegitBridge</h1>

<div align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.16.5%20%7C%201.20.1-brightgreen?logo=minecraft" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/API-Fabric-orange" alt="Fabric">
  <img src="https://img.shields.io/github/license/Abdullaherain0131/LegitBridge-MultiVersion" alt="License">
</div>

<p align="center">
  <strong>LegitBridge</strong> is a modern, undetectable multi-version Fabric mod designed to enhance your gameplay with legit mechanics and helpful utilities. Say goodbye to manual souping and tedious interactions.
</p>

## ✨ Features

- **🛡️ LegitAutoPot (Auto Soup)**
  - Automatically consumes soups when health drops below a configurable threshold.
  - Features smart slot restoration and automatic inventory refill mechanics.
  - Drops empty bowls seamlessly! (Works flawlessly on SoupPvP servers).
- **⚔️ LegitAutoClicker**
  - Advanced human-like clicking patterns with customizable CPS (Clicks Per Second).
  - Configurable jitter, randomization, and item whitelists.
- **🏃 InvMove**
  - Move around, sprint, jump, and interact while having any GUI or inventory open.
- **💡 Fullbright**
  - Brightens the world without the need for torches or night vision potions.
- **🤪 Derp**
  - Send spoofed rotation and animation packets to the server. Spin wildly for others while playing normally!
- **🦅 Freecam (Work in Progress)**
  - Detach your camera from your player to look around corners and explore freely.

## 🚀 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft `1.16.5` or `1.20.1`.
2. Download the latest `LegitBridge` `.jar` from the [Releases](#) tab (or build it yourself!).
3. Drop the `.jar` into your `.minecraft/mods` folder.
4. Launch the game and access the mod configuration menu!

*(Insert GIF of AutoSoup or Mod Menu here)*

## 🛠️ Building from Source

This project uses [Stonecutter](https://stonecutter.kikugie.dev/) to support multiple Minecraft versions from a single codebase!

```bash
git clone https://github.com/Abdullaherain0131/LegitBridge-MultiVersion.git
cd LegitBridge-MultiVersion
# Build for 1.16.5
./gradlew :1.16.5:build
# Or build for 1.20.1
./gradlew :1.20.1:build
```
The compiled jar will be available in `versions/<version>/build/libs`.

## 📜 License
This project is licensed under the [MIT License](LICENSE).

---
*For Turkish documentation, please check out [README-tr.md](README-tr.md).*
