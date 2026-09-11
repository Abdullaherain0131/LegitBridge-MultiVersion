package com.ersin.legitbridge;

import java.util.Random;

public class Humanizer {
    private static final Random random = new Random();
    private static int clickCount = 0;

    /**
     * Düz rastgelelik yerine Gauss (Normal) dağılım ile gecikme üretir.
     * Bu, ortalama (mean) değere yakın sonuçlar üretirken, nadiren uç değerler üretir,
     * tıpkı gerçek bir insanın tıklama aralıkları gibi.
     */
    public static int getGaussianDelay(int min, int max) {
        if (min >= max) return min;

        double mean = (min + max) / 2.0;
        double stdDev = (max - min) / 4.0; // Standart sapma
        
        // Gauss dağılımlı rastgele sayı
        double val = mean + (random.nextGaussian() * stdDev);
        
        // Değeri min ve max arasına sıkıştır (clamp)
        int result = (int) Math.round(Math.max(min, Math.min(max, val)));
        
        clickCount++;
        
        // İnsan yorgunluğu: Her ~20 tıklamada bir, hafif takılma (drop) efekti ekle (+1 veya +2 tick)
        if (clickCount > 20 && random.nextInt(100) < 15) { 
            clickCount = 0; // Sıfırla
            result += (1 + random.nextInt(2)); 
        }

        return result;
    }

    /**
     * Anti-cheat'leri şaşırtmak için çok düşük bir ihtimalle tıklamanın kasten atlanmasını sağlar.
     */
    public static boolean shouldDropClick() {
        // %2.5 ihtimalle tıklamayı düşür
        return random.nextFloat() < 0.025f;
    }
    
    /**
     * Smooth jittering hesaplar. Bir önceki değeri yavaşça hedefe çeker.
     */
    public static double getSmoothedJitter(double current, double min, double max) {
        double mean = (min + max) / 2.0;
        double stdDev = (max - min) / 6.0;
        double target = mean + (random.nextGaussian() * stdDev);
        target = Math.max(min, Math.min(max, target));
        
        // Yumuşak geçiş (Interpolation)
        double smoothingFactor = 0.15; // Değişim hızı
        return current + (target - current) * smoothingFactor;
    }
}
