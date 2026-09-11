package com.ersin.legitbridge;

public class SessionTracker {
    public static final long sessionStartTime = System.currentTimeMillis();
    public static int totalBlocksPlaced = 0;
    public static int maxCps = 0;

    public static void onBlockPlaced() {
        totalBlocksPlaced++;
    }

    public static void checkCps(int currentCps) {
        if (currentCps > maxCps) {
            maxCps = currentCps;
        }
    }

    public static String getFormattedSessionTime() {
        long elapsedSeconds = (System.currentTimeMillis() - sessionStartTime) / 1000;
        long hours = elapsedSeconds / 3600;
        long minutes = (elapsedSeconds % 3600) / 60;
        long seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
