/**
 * Copyright (C), 2019-2019, 贵州宏思锐达科技有限公司
 * FileName: MessageUtil
 * Author:   Arron-Wu
 * Date:     2019/5/19 11:48
 * Description: 将xml转换成map
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.stylefeng.guns.util.wechat_public;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈公众号消息工具类〉
 *
 * @author Arron
 * @create 2019/5/19
 * @since 1.0.0
 */
public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK= "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";


    /**

     * 新建方法，将接收到的XML格式，转化为Map对象

     * @param request 将request对象，通过参数传入

     * @return 返回转换后的Map对象

     */

    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {

        Map<String, String> map = new HashMap<String, String>();

        //从dom4j的jar包中，拿到SAXReader对象。
        SAXReader reader = new SAXReader();



        InputStream is = request.getInputStream();//从request中，获取输入流

        Document doc =  reader.read(is);//从reader对象中,读取输入流

        Element root = doc.getRootElement();//获取XML文档的根元素

        List<Element> list = root.elements();//获得根元素下的所有子节点

        for (Element e : list) {

            map.put(e.getName(), e.getText());//遍历list对象，并将结果保存到集合中

        }

        is.close();

        return map;

    }
    /**

     * 将文本消息对象转化成XML格式

     * @param message 文本消息对象

     * @return 返回转换后的XML格式

     */

    public static String objectToXml(TextMessage message){

        XStream xs = new XStream();

        //由于转换后xml根节点默认为class类，需转化为<xml>

        xs.alias("xml", message.getClass());

        return xs.toXML(message);

    }

    public static String initText(String ToUserName,String FromUserName,String Content){
        TextMessage message = new TextMessage();

        message.setFromUserName(ToUserName);//原来【接收消息用户】变为回复时【发送消息用户】

        message.setToUserName(FromUserName);

        message.setMsgType(MessageUtil.MESSAGE_TEXT);

        message.setCreateTime(new Date().getTime());//创建当前时间为消息时间

        message.setContent(Content);

        return MessageUtil.objectToXml(message); //调用Message工具类，将对象转为XML字符串
    }

    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("这里是暖通售后服务公众品台，感谢您的关注！");
        //如果有菜单  在这里写.
        //sb.append("1.关于我们");
        return sb.toString();
    }
}