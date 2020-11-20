import sun.misc.BASE64Decoder;

import java.io.*;

public class Base64FileUtils {
    /**
     * 将base64转化为图片
     *
     * @param imgData
     * @param imgFilePath
     * @return
     * @throws IOException
     */
    public static boolean GenerateImage(String imgData, String imgFilePath) throws IOException { // 对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            out.flush();
            out.close();
        }
        return true;
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteAll(String dirPath) {
        File dir = new File(dirPath);//获取文件夹
        File[] files = dir.listFiles();
        if (files.length == 0)//没有文件
            return false;

        for (File file : files) {
            file.delete();
        }

        return true;
    }

}
