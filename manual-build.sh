#!/bin/bash

echo "======================================"
echo "SimpleCobbleGen Manuel Derleme"
echo "======================================"
echo ""

# Klasörleri hazırla
echo "[1/4] Klasörler hazırlanıyor..."
rm -rf target
mkdir -p target/classes
mkdir -p target/classes/com/skyblock/cobblegen/commands
mkdir -p target/classes/com/skyblock/cobblegen/listeners
mkdir -p target/classes/com/skyblock/cobblegen/managers
mkdir -p target/classes/com/skyblock/cobblegen/models
mkdir -p target/classes/com/skyblock/cobblegen/gui

# Resources'ı kopyala
echo "[2/4] Config dosyaları kopyalanıyor..."
cp -r src/main/resources/* target/classes/

# Java dosyalarını derle (Spigot API olmadan - sadece interface'ler için)
echo "[3/4] Java dosyaları derleniyor..."

# Not: Gerçek derleme için Spigot JAR gerekli, ama kaynak kodları hazırlıyoruz
cat > target/classes/COMPILE-INFO.txt << 'EOF'
Bu plugin'i derlemek için:

SEÇENEK 1 - Maven (Önerilen):
  mvn clean package

SEÇENEK 2 - IDE ile:
  1. IntelliJ IDEA veya Eclipse'i açın
  2. Projeyi import edin
  3. Maven dependencies'i yükleyin
  4. Build → Build Artifacts

SEÇENEK 3 - Online:
  1. replit.com veya glitch.com gibi bir platforma yükleyin
  2. Maven ile build edin

Gereksinimler:
- Java 8+
- Maven 3.6+
- Spigot API 1.20.1
- Vault API

Derlenmiş JAR dosyası burada oluşacak:
  target/SimpleCobbleGen-1.0.0.jar
EOF

echo "[4/4] Kaynak kodlar paketleniyor..."

# Kaynak kodları jar olarak pakelle (çalıştırılabilir değil, sadece kaynak)
cd src/main/java
jar cf ../../../target/SimpleCobbleGen-SOURCE.jar .
cd ../../..

cp -r src/main/resources/* target/classes/

echo ""
echo "✓ Tamamlandı!"
echo ""
echo "DİKKAT: Bu sadece kaynak kodlarıdır."
echo "Çalıştırılabilir JAR için Maven kullanın:"
echo "  cd SimpleCobbleGen"
echo "  mvn clean package"
echo ""
echo "Çıktılar:"
echo "  - target/SimpleCobbleGen-SOURCE.jar (Kaynak kodlar)"
echo "  - target/classes/ (Config dosyaları)"
echo ""
