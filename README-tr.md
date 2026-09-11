<h1 align="center">LegitBooster</h1>

<div align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.16.5%20%7C%201.20.1-brightgreen?logo=minecraft" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/API-Fabric-orange" alt="Fabric">
  <img src="https://img.shields.io/github/license/Abdullaherain0131/LegitBridge-MultiVersion" alt="License">
</div>

<p align="center">
  <strong>LegitBooster</strong>, tamamen "legit" (fark edilmez) oyun mekaniklerine odaklanmış modern bir çoklu-sürüm Fabric modudur. Manuel çorba içme derdine ve sinir bozucu kısıtlamalara elveda deyin.
</p>

## ✨ Özellikler

- **🛡️ LegitAutoPot (Oto Çorba / Auto Soup)**
  - Canın belirlenen seviyenin altına düştüğünde otomatik olarak çorba içer.
  - Akıllı slot yenileme sistemi: Envanterindeki çorbaları otomatik olarak boş slotlara çeker.
  - İçilen boş kaseleri anında ve sorunsuz bir şekilde yere atar! (SoupPvP sunucularında kusursuz çalışır).
- **⚔️ LegitAutoClicker**
  - İnsan vuruşlarına benzeyen (humanized) tıklama desenleri ve ayarlanabilir CPS (Saniyedeki Tıklama Sayısı).
  - Jitter, rastgeleleştirme ve sadece kılıç/balta ile çalışma (whitelist) özellikleri.
- **🏃 InvMove (Envanterde Hareket)**
  - Envanter, sandık veya herhangi bir menü açıkken bile yürüyebilir, koşabilir ve zıplayabilirsiniz.
- **💡 Fullbright**
  - Gece görüş iksirine veya meşaleye ihtiyaç duymadan dünyayı tamamen aydınlatır.
- **🤪 Derp**
  - Sunucuya rastgele dönüş (rotation) ve animasyon paketleri gönderir. Sen oyunu normal oynarken dışarıdan delicesine dönüyormuş gibi görünürsün!
- **🦅 Freecam (Geliştirme Aşamasında)**
  - Kameranı oyuncu karakterinden ayırıp serbestçe uçarak etrafı keşfetmeni sağlar.

## 🚀 Kurulum

1. Minecraft `1.16.5` veya `1.20.1` için [Fabric Loader](https://fabricmc.net/use/)'ı kur.
2. [Releases](#) sekmesinden en güncel `LegitBooster` `.jar` dosyasını indir (veya kendin derle!).
3. İndirdiğin `.jar` dosyasını `.minecraft/mods` klasörünün içine at.
4. Oyunu başlat ve menüden mod ayarlarına göz at!

*(Buraya AutoSoup veya Mod Menüsü GIF'i eklenebilir)*

## 🛠️ Kaynak Koddan Derleme (Building)

Bu proje tek bir kod tabanından birden fazla Minecraft sürümünü desteklemek için [Stonecutter](https://stonecutter.kikugie.dev/) altyapısını kullanır!

```bash
git clone https://github.com/Abdullaherain0131/LegitBridge-MultiVersion.git
cd LegitBridge-MultiVersion
# 1.16.5 için derle
./gradlew :1.16.5:build
# Veya 1.20.1 için derle
./gradlew :1.20.1:build
```
Derlenen mod dosyası `versions/<version>/build/libs` klasöründe bulunabilir.

## 📜 Lisans
Bu proje [CC0 1.0 Universal (Public Domain)](LICENSE) altında lisanslanmıştır. Daha fazla detay için dosyayı inceleyebilirsiniz.

---
*English documentation is available at [README.md](README.md).*
