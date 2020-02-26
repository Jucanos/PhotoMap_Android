package com.jucanos.photomap.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jucanos.photomap.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Administrator on 11/2/2015.
 */
public class BitmapUtils {

    /**
     * Get the file path from the given Uri.
     *
     * @param context The context of the calling activity.
     * @param uri     The Uri whose file path is returned.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getFilePathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {

            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Write the given bitmap into the given file. JPEG is used as the compression format with
     * quality set
     * to 100.
     *
     * @param bm   The bitmap.
     */
    //create a file to write bitmap data
    public static String saveBitmap(String filename, Bitmap bm,int quality,Context mContext) throws IOException {
        File f = new File(mContext.getCacheDir(), filename);
        f.createNewFile();

        //Convert bitmap to byte array
        Bitmap bitmap = bm;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static Bitmap addPadding(@NonNull Bitmap bmp) {
        int biggerParam = Math.max(bmp.getWidth(), bmp.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(biggerParam, biggerParam, bmp.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        int top = bmp.getHeight() > bmp.getWidth() ? 0 : (bmp.getWidth() - bmp.getHeight()) / 2;
        int left = bmp.getWidth() > bmp.getHeight() ? 0 : (bmp.getHeight() - bmp.getWidth()) / 2;

        canvas.drawBitmap(bmp, left, top, null);
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getBitmapByPath(String path){
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;
        }else{
            return null;
        }
    }

    public static Bitmap getBitmapByPathMutable(String path){
        File imgFile = new File(path);
        BitmapFactory.Options opts = new BitmapFactory.Options(); opts.inMutable = true;
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),opts);
            return myBitmap;
        }else{
            return null;
        }
    }

    public static void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName,Context context) throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/photoMap");
        myDir.mkdirs();
        String fname = fileName;
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        file.createNewFile();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            addPicToGallery(context,file.getPath());
            Toast.makeText(context, "저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "저장 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void addPicToGallery(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static float pxToDp(Context context, float px) {
        // 해상도 마다 다른 density 를 반환. xxxhdpi는 density = 4
        float density = context.getResources().getDisplayMetrics().density;
        if (density == 1.0)      // mpdi  (160dpi) -- xxxhdpi (density = 4)기준으로 density 값을 재설정 한다
            density *= 4.0;
        else if (density == 1.5) // hdpi  (240dpi)
            density *= (8.0 / 3);
        else if (density == 2.0) // xhdpi (320dpi)
            density *= 2.0;
        return px / density;     // dp 값 반환
    }

    public static void saveViewImage(Context context,View v) throws IOException {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        BitmapUtils.createDirectoryAndSaveFile(b, fileName, context);
    }
}
