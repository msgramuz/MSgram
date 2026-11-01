package org.telegram.messenger;

import android.text.TextUtils;

import org.telegram.tgnet.TLRPC;

import java.io.File;

/**
 * MSgram StopVirus.
 *
 * APK files passed around in chats are the usual vector for malware here ("tuy.apk" and
 * friends), so when this is on the client refuses to auto-download them, refuses to hand
 * one to the installer, and removes any that already landed in the app's own storage.
 *
 * It only ever touches files inside the app's own download directories.
 */
public class StopVirus {

    public static final String APK_MIME = "application/vnd.android.package-archive";

    public static boolean isEnabled() {
        return SharedConfig.stopVirusEnabled;
    }

    public static boolean isApk(String fileName, String mimeType) {
        if (!TextUtils.isEmpty(mimeType) && APK_MIME.equalsIgnoreCase(mimeType.trim())) {
            return true;
        }
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        final String lower = fileName.trim().toLowerCase();
        // .apks / .xapk / .apkm are split-install bundles, same risk
        return lower.endsWith(".apk") || lower.endsWith(".apks")
                || lower.endsWith(".xapk") || lower.endsWith(".apkm");
    }

    public static boolean isApk(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        return isApk(FileLoader.getDocumentFileName(document), document.mime_type);
    }

    /** Deletes an APK that already reached storage. Safe to call with anything. */
    public static boolean deleteIfApk(File file, String fileName, String mimeType) {
        if (file == null || !file.exists() || !isApk(fileName != null ? fileName : file.getName(), mimeType)) {
            return false;
        }
        try {
            return file.delete();
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    /**
     * Sweeps the app's download directories for APKs. Returns how many were removed.
     * Must not run on the main thread.
     */
    public static int cleanDownloads() {
        int removed = 0;
        final int[] dirs = new int[]{
                FileLoader.MEDIA_DIR_DOCUMENT,
                FileLoader.MEDIA_DIR_CACHE,
                FileLoader.MEDIA_DIR_FILES
        };
        for (int type : dirs) {
            File dir;
            try {
                dir = FileLoader.checkDirectory(type);
            } catch (Exception e) {
                continue;
            }
            removed += sweep(dir, 0);
        }
        return removed;
    }

    private static int sweep(File dir, int depth) {
        if (dir == null || !dir.isDirectory() || depth > 3) {
            return 0;
        }
        final File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        int removed = 0;
        for (File f : files) {
            if (f.isDirectory()) {
                removed += sweep(f, depth + 1);
            } else if (isApk(f.getName(), null)) {
                try {
                    if (f.delete()) {
                        removed++;
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
        return removed;
    }
}
