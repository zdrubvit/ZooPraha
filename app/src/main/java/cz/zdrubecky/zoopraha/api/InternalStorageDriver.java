package cz.zdrubecky.zoopraha.api;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorageDriver {
    private static final String FILE_NAME = "processed_resources";

    // Check if the resource has already been handled according to its ETag value, stored in a local storage
    public static boolean wasResourceProcessed(Context context, String etag) {
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            // Use StringBuilder instead of StringBuffer because there's no need for thread safety
            StringBuilder fileContents = new StringBuilder("");
            byte[] buffer = new byte[1024];
            int n;

            try {
                while ((n = fileInputStream.read(buffer)) != -1) {
                    fileContents.append(new String(buffer, 0, n));
                }

                fileInputStream.close();

                return fileContents.indexOf(etag) >= 0;
            } catch (IOException ioe) {

            }
        } catch (FileNotFoundException fnf) {

        }

        return false;
    }

    // Store the ETag value to use it for comparisons later
    public static void storeProcessedResource(Context context, String etag) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);

            try {
                fileOutputStream.write(etag.getBytes());
                fileOutputStream.close();
            } catch (IOException ioe) {

            }
        } catch (FileNotFoundException fnf) {

        }
    }

    // Delete the file with processed resources, which invalidates the skipping of API responses with a 304 HTTP code
    public static boolean deleteProcessedResourcesFile(Context context) {
        File directory = context.getFilesDir();
        File file = new File(directory, FILE_NAME);

        return file.delete();
    }
}
