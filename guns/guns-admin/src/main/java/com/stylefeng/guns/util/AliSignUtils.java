package com.stylefeng.guns.util;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AliSignUtils {
    public static String getSignature(Map parameters,String AccessKeySecret) {
        String signature = "";
        try {
            // 对参数进行排序
            String[] sortedKeys = (String[]) parameters.keySet().toArray(new String[]{});
            Arrays.sort(sortedKeys);
            final String SEPARATOR = "&";
            // 生成stringToSign字符串
            StringBuilder stringToSign = new StringBuilder();

            stringToSign.append("GET").append(SEPARATOR);

            stringToSign.append(percentEncode("/")).append(SEPARATOR);

            StringBuilder canonicalizedQueryString = new StringBuilder();

            for (String key : sortedKeys) {
                // 这里注意对key和value进行编码
                canonicalizedQueryString.append("&").append(percentEncode(key))
                        .append("=").append(percentEncode((String) parameters.get(key)));
            }
            // 这里注意对canonicalizedQueryString进行编码
            stringToSign.append(percentEncode(canonicalizedQueryString.toString()
                    .substring(1)));
            System.err.println("----------stringToSign:"+ stringToSign.toString());
            final String ALGORITHM = "HmacSHA1";
            final String ENCODING = "UTF-8";
            String key = AccessKeySecret + "&";
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));
            byte[] signData = mac.doFinal(stringToSign.toString().getBytes(ENCODING));
            signature = new String(new BASE64Encoder().encode(signData));
            signature = percentEncode(signature);
            System.err.println("----------signature:"+ signature);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, "utf-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;

    }

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String getUTCTimeStr() {
        //1、取得本地时间：
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        System.out.println(cal.getTime());
        //2、取得时间偏移量：
        final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        System.out.println(zoneOffset);
        //3、取得夏令时差：
        final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        System.out.println(dstOffset);
        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        return df.format(cal.getTime());
    }

    /**
     * 将request中的参数转换成Map
     * @param request
     * @return
     */
    public static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        return params;
    }
}
