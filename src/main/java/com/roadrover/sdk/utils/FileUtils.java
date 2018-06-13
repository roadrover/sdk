package com.roadrover.sdk.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件的工具类
 * 实现和文件相关的方法，目前主要只实现了文件拷贝的方法
 * @author bin.xie
 * @date 2016/4/25
 */
public class FileUtils {

    public FileUtils() {
        // TODO Auto-generated constructor stub
    }

    public interface FileOptionProgressCallback {
        void onOptionProgress(long totalSize, long proSizes);
    }

    /**
     * 拷贝 assets 下面的文件到本机，最终目录为 /data/data/包名/files/assets/
     * @param context
     * @param assetsFileName assets下面需要拷贝的文件名
     * @param destFileName 目标文件名
     * @return 拷贝完之后，返回拷贝结果目录
     */
    public static String copyAssetsFileToLocal(Context context, String assetsFileName, String destFileName) {
        if (context == null) {
            return "";
        }

        String dir = context.getFilesDir().toString() + "/assets/";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String destFilePath = dir + destFileName;
        if (!(new File(destFilePath)).exists()) { // 文件不存在，从assets下面拷贝过来
            try {
                if (fileChannelCopy(context.getAssets().open(assetsFileName), destFilePath)) {
                    return destFilePath;
                } else {
                    return "";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return destFilePath;
    }

    /**
     * 使用文件通道的方式复制文件
     * @param in
     * @param destPath
     * @return
     */
    public static boolean fileChannelCopy(InputStream in, String destPath) {
        if (in == null || TextUtils.isEmpty(destPath)) {
            return false;
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(destPath);
            byte[] buffer = new byte[4096];
            int len;
            while((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 拷贝文件
     * @param srcPath
     * @param destPath
     * @return
     */
    public static boolean copyFile(String srcPath, String destPath) {
        return copyFile(srcPath, destPath, true);
    }

    public static boolean copyFile(String srcPath, String destPath, boolean overlay) {
        return copyFile(srcPath, destPath, overlay, null);
    }

    /**
     * 拷贝文件
     * @param srcPath 原目录
     * @param destPath 目标目录
     * @param overlay 覆盖文件
     * @throws Exception
     */
    public static boolean copyFile(String srcPath, String destPath, boolean overlay, FileOptionProgressCallback callback) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(destPath)) {
            return false;
        }
        File srcFile = new File(srcPath);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            Logcat.d("srcFile:" + srcPath + " not exists!");
            return false;
        } else if (!srcFile.isFile()) {
            Logcat.d("srcFile:" + srcPath + " not file!");
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destPath);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                if (TextUtils.equals(srcPath, destPath)) { // 排除同目录下复制
                    return true;
                }
                new File(destPath).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        long total = srcFile.length();
        long pro = 0;

        FileInputStream in = null;
        FileOutputStream out = null;
        int byteread = 0; // 读取的字节数
        try {
            in = new FileInputStream(srcPath);
            out = new FileOutputStream(destPath);
            byte[] buffer = new byte[8192];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
                if (callback != null) {
                    pro += byteread;
                    callback.onOptionProgress(total, pro);
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] childs = file.listFiles();
                for (File child : childs) {
                    deleteFile(child);
                }
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if(!f.exists()){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 从文件里面读取 readSize 大小的字符
     * @param fileName 文件名
     * @param readSize 读取大小
     * @return 返回读取内容
     */
    public static String readFile(String fileName, int readSize) {
        if (TextUtils.isEmpty(fileName) || readSize < 0) {
            Logcat.w("fileName:" + fileName + " readSize:" + readSize);
            return "";
        }
        File file = new File(fileName);
        if (!file.exists() || !file.canRead()) {
//            Logcat.w("file:" + fileName + " not exists or not canRead!");
            return "";
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[readSize];
            in.read(buffer);

            String str = new String(buffer);
            // Logcat.d(fileName + ": " + str);
            return str;
        } catch (Exception e){
            Logcat.e("exception at read file " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 往文件里面写一个字符
     * @param filePath 指定路径
     * @param value 写的字符
     */
    public static void writeFile(String filePath, String value) {
        if (TextUtils.isEmpty(filePath) || value == null) {
            Logcat.w("filePath:" + filePath + " value:" + value);
            return ;
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            byte[] bytes = value.getBytes();
            out.write(bytes);
        } catch (IOException e) {
            Logcat.e("exception at write register file");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
