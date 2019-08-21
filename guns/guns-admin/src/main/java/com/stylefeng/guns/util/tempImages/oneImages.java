package com.stylefeng.guns.util.tempImages;/*
package com.stylefeng.guns.core.util.tempImages;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

*/
/**
 * Created by Heyifan Cotter on 2018/10/15.
 * 封装海报生成方法
 *//*

public class oneImages {
    public oneImages() {

    }

    private String title;
    private String description;
    private String picture;
    private String columTypeName;

    */
/*
     * public final static String getPressImgPath() { return ApplicationContext
     * .getRealPath("/template/data/util/shuiyin.gif"); }
     *//*


    */
/**
     * 把图片印刷到图片上
     *
     * @param pressImg  --
     *                  水印文件
     * @param targetImg --
     *                  目标文件
     * @param x         --x坐标
     * @param y         --y坐标
     *//*

    public final static void pressImage(String pressImg, String targetImg, int x, int y) {
        try {
            //目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            URL url = new URL(pressImg);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            //水印文件
//            File _filebiao = new File(pressImg);
//            System.out.println(_filebiao.exists());
            Image src_biao = ImageIO.read(inStream);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, 536 + 55 + 25, 642 + 52 + 213, 152, 151, null);
            //水印文件结束
            g.dispose();
            FileOutputStream out = new FileOutputStream("F:/images/wosnbb.jpg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 打印文字水印图片
     *
     * @param pressText --文字
     * @param targetImg --
     *                  目标图片
     * @param fontName  --
     *                  字体名
     * @param fontStyle --
     *                  字体样式
     * @param color     --
     *                  字体颜色
     * @param fontSize  --
     *                  字体大小
     * @param x         --
     *                  偏移量
     * @param y
     *//*


    public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) {
        try {

            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // String s="www.qhd.com.cn";
            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        pressImage("http://cheshi654321.oss-cn-beijing.aliyuncs.com/data/1539593701464.png", "F:/images/8.jpg", 50, 90);

    }
}
*/
