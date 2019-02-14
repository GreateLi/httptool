/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.http.tools.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageUtils {

    public static Uri geturi(android.content.Intent intent, Context context) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/*"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                    System.out.println("---");
                } else {
                    Uri uriTemp = Uri.parse("content://media/external/images/media/" + index);
                    if (uriTemp != null) {
                        uri = uriTemp;
                    }
                }
            }
        }
        return uri;
    }

    public static void resize(Bitmap bitmap, File outputFile, int maxWidth, int maxHeight) {
        try {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 图片大于最大高宽，按大的值缩放
            if (bitmapWidth > maxHeight || bitmapHeight > maxWidth) {
                float widthScale = maxWidth * 1.0f / bitmapWidth;
                float heightScale = maxHeight * 1.0f / bitmapHeight;

                float scale = Math.min(widthScale, heightScale);
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            }

            // save image
            FileOutputStream out = new FileOutputStream(outputFile);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rgb2Bitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 4 + 0] = b1;
            rgba[i * 4 + 1] = b2;
            rgba[i * 4 + 2] = b3;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    public static byte[]  getRGBBuffer(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height *3];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 3+ 0] = b1;
            rgba[i * 3 + 1] = b2;
            rgba[i * 3 + 2] = b3;
            // rgba[i * 4 + 3] = (byte) 255;
        }
        //  stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return rgba;//stitchBmp;
    }

    public static Bitmap dethRGB2Bitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 2 + 0];
            byte b2 = bytes[i * 2 + 1];
            short tmp = (short) ((short) b1 + (short)(b2<<8));
            // set value
            rgba[i * 4 + 0] = (byte)(tmp/16);
            rgba[i * 4 + 1] = (byte)0;
            rgba[i * 4 + 2] = (byte)0;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    public static byte[] dethRGB2Buffer(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 3];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 2 + 0];
            byte b2 = bytes[i * 2 + 1];
            short tmp = (short) ((short) b1 + (short)(b2<<8));
            // set value
            rgba[i * 3 + 0] = (byte)(tmp/16);
            rgba[i * 3 + 1] = (byte)0;
            rgba[i * 3 + 2] = (byte)0;
            // rgba[i * 4 + 3] = (byte) 255;
        }
        //  stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return rgba;
    }

    public static   void autoSaveImage(Bitmap bitmap, long time,float rgbLivenessScore,boolean [] isCompliteOk,Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断是否可以对SDcard进行操作
            // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/AutoTrackImage/";
            File dirFile = new File(sdCardDir);  // 目录转化成文件夹
            if (!dirFile.exists()) {                // 如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            String groupDir = time + "";
            File groupFile = new File(dirFile, groupDir);
            if (!groupFile.exists()) {                // 如果不存在，那就建立这个文件夹
                groupFile.mkdirs();
            }
            String path = sdCardDir + groupDir; // 文件夹有啦，就可以保存图片啦
            File file = new File(path, time + "_rgb_" + rgbLivenessScore + ".jpg"); // 在SDcard的目录下创建图片文,以当前时间为其命名
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (out!=null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isCompliteOk[0] = true;
            Toast.makeText(context,
                    "保存已经至" + Environment.getExternalStorageDirectory()
                            + "/AutoTrackImage/" + "目录文件夹下", Toast.LENGTH_SHORT).show();
        }

    }

    public static   void saveImage(Bitmap bitmap, long time,float rgbLivenessScore,boolean [] isCompliteOk,Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断是否可以对SDcard进行操作
            // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/TrackImage/";
            File dirFile = new File(sdCardDir);  // 目录转化成文件夹
            if (!dirFile.exists()) {                // 如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }

            String groupDir = time + "";
            File groupFile = new File(dirFile, groupDir);
            if (!groupFile.exists()) {                // 如果不存在，那就建立这个文件夹
                groupFile.mkdirs();
            }
            String path = sdCardDir + groupDir;
            File file = new File(path, time + "_rgb_" + rgbLivenessScore + ".jpg"); // 在SDcard的目录下创建图片文,以当前时间为其命名
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (out!=null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isCompliteOk[0] = true;
            Toast.makeText(context,
                    "保存已经至" + Environment.getExternalStorageDirectory()
                            + "/TrackImage/" + "目录文件夹下", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    public static   void createFileWithByte(byte[] bytes, long time,float depthLivenessScore,boolean [] isCompliteOk) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/AutoTrackImage/";
            String fileName = time + "_depthData_" + depthLivenessScore;
            File dirFile = new File(sdCardDir);  // 目录转化成文件夹
            if (!dirFile.exists()) {                // 如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            String groupDir = time + "";
            File groupFile = new File(dirFile, groupDir);
            if (!groupFile.exists()) {                // 如果不存在，那就建立这个文件夹
                groupFile.mkdirs();
            }
            String path = sdCardDir + groupDir;
            // 判断是否可以对SDcard进行操作
            File file = new File(path, fileName);
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            // 创建BufferedOutputStream对象
            BufferedOutputStream bufferedOutputStream = null;
            try {
                // 如果文件存在则删除
                if (file.exists()) {
                    file.delete();
                }
                // 在文件系统中根据路径创建一个新的空文件
                file.createNewFile();
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(file);
                // 获取BufferedOutputStream对象
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                // 往文件所在的缓冲输出流中写byte数据
                bufferedOutputStream.write(bytes);
                // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                bufferedOutputStream.flush();
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                isCompliteOk[0] = true;
            }
        }
    }


    public static   void saveDepthImage(Context context,Bitmap bitmap, long time,float depthLivenessScore,boolean [] isCompliteOk) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 判断是否可以对SDcard进行操作
            // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/TrackImage/";
            File dirFile = new File(sdCardDir);  // 目录转化成文件夹
            if (!dirFile.exists()) {                // 如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            String groupDir = time + "";
            File groupFile = new File(dirFile, groupDir);
            if (!groupFile.exists()) {                // 如果不存在，那就建立这个文件夹
                groupFile.mkdirs();
            }
            String path = sdCardDir + groupDir;
            File file = new File(path, time + "_depth_" + depthLivenessScore + ".jpg"); // 在SDcard的目录下创建图片文,以当前时间为其命名
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                if (out!=null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            isCompliteOk[0] = true;
            Toast.makeText(context,
                    "保存已经至" + Environment.getExternalStorageDirectory()
                            + "/TrackImage/" + "目录文件夹下", Toast.LENGTH_SHORT).show();
        }

    }


    public static  void createFileWithDepthByte(byte[] bytes, long time, float depthLivenessScore,boolean [] isCompliteOk) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/TrackImage/";
            String fileName = time + "_depthData_" + depthLivenessScore;
            File dirFile = new File(sdCardDir);  // 目录转化成文件夹
            if (!dirFile.exists()) {                // 如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            String groupDir = time + "";
            File groupFile = new File(dirFile, groupDir);
            if (!groupFile.exists()) {                // 如果不存在，那就建立这个文件夹
                groupFile.mkdirs();
            }
            String path = sdCardDir + groupDir;
            // 判断是否可以对SDcard进行操作
            File file = new File(path, fileName);
            // 创建FileOutputStream对象
            FileOutputStream outputStream = null;
            // 创建BufferedOutputStream对象
            BufferedOutputStream bufferedOutputStream = null;
            try {
                // 如果文件存在则删除
                if (file.exists()) {
                    file.delete();
                }
                // 在文件系统中根据路径创建一个新的空文件
                file.createNewFile();
                // 获取FileOutputStream对象
                outputStream = new FileOutputStream(file);
                // 获取BufferedOutputStream对象
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                // 往文件所在的缓冲输出流中写byte数据
                bufferedOutputStream.write(bytes);
                // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
                bufferedOutputStream.flush();
            } catch (Exception e) {
                // 打印异常信息
                e.printStackTrace();
            } finally {
                // 关闭创建的流对象
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                isCompliteOk[0] = true;
            }
        }
    }

    public static Bitmap cameraByte2Bitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }
}
