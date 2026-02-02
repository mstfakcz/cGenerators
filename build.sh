#!/bin/bash
echo "SimpleCobbleGen derleniyor..."
echo ""

# Maven yüklü mü kontrol et
if ! command -v mvn &> /dev/null
then
    echo "Maven bulunamadı! Manuel derleme yapılacak..."
    
    # Manuel JAR oluşturma
    mkdir -p target/classes
    
    # Java dosyalarını derle
    find src/main/java -name "*.java" > sources.txt
    javac -d target/classes -cp ".:*" @sources.txt 2>/dev/null || {
        echo "HATA: Java derlemesi başarısız!"
        echo ""
        echo "Maven ile derlemeyi deneyin:"
        echo "  mvn clean package"
        exit 1
    }
    
    # Resources'ı kopyala
    cp -r src/main/resources/* target/classes/
    
    # JAR oluştur
    cd target/classes
    jar cf ../SimpleCobbleGen-1.0.0.jar *
    cd ../..
    
    echo "✓ Manuel derleme tamamlandı!"
    echo "JAR dosyası: target/SimpleCobbleGen-1.0.0.jar"
else
    # Maven ile derle
    mvn clean package -q
    
    if [ $? -eq 0 ]; then
        echo "✓ Maven derlemesi başarılı!"
        echo "JAR dosyası: target/SimpleCobbleGen-1.0.0.jar"
    else
        echo "✗ Maven derlemesi başarısız!"
        exit 1
    fi
fi

echo ""
echo "Plugin hazır! SimpleCobbleGen-1.0.0.jar dosyasını sunucunuzun plugins klasörüne kopyalayın."
