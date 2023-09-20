package com.gelora.absensi.support;
import android.content.Context;
import java.io.File;

public class CacheUtil {

    public static void clearCacheExt(Context context) {
        try {
            // Clear the app's internal cache directory
            context.getCacheDir().delete();

            // Clear the app's external cache directory if it exists
            if (context.getExternalCacheDir() != null) {
                context.getExternalCacheDir().delete();
                clearCache(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearCache(Context context) {
        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteDir(cacheDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}

