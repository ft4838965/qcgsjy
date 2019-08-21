package com.stylefeng.guns.util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Heyifan Cotter on 2019/3/19.
 */
public class ImgUtil {
    /**
     * 将图片处理为圆形图片
     * 传入的图片必须是正方形的才会生成圆形 如果是长方形的比例则会变成椭圆的
     * 
     * @param url
     * @return
     */
    public static BufferedImage transferImgForRoundImgage(String url){
        BufferedImage resultImg = null;
        try {
            if (StringUtils.isBlank(url)) {
                return null;
            }
            BufferedImage buffImg1 = ImageIO.read(new URL(url));
            resultImg = new BufferedImage(buffImg1.getWidth(), buffImg1.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resultImg.createGraphics();
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, buffImg1.getWidth(), buffImg1.getHeight());
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            resultImg = g.getDeviceConfiguration().createCompatibleImage(buffImg1.getWidth(), buffImg1.getHeight(),
                    Transparency.TRANSLUCENT);
            //g.fill(new Rectangle(buffImg2.getWidth(), buffImg2.getHeight()));
            g = resultImg.createGraphics();
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setClip(shape);
            g.drawImage(buffImg1, 0, 0, null);
            g.dispose();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultImg;
    }
}
