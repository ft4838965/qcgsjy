package com.stylefeng.guns.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.stylefeng.guns.modular.setting.service.ISettingService;
import com.stylefeng.guns.modular.system.model.Setting;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tool {
	public static void deleteAliVideoImgUrl(String ImageURLs,String AccessId,String AccessKey){
		String SignatureNonce= UuidUtil.get32UUID();
		String Timestamp= AliSignUtils.getUTCTimeStr();
		Map parameters = new HashMap();
		parameters.put("Action","DeleteImage");
		parameters.put("DeleteImageType","ImageURL");
		parameters.put("ImageURLs",ImageURLs);

		parameters.put("Format","JSON");
		parameters.put("Version","2017-03-21");
		parameters.put("AccessKeyId",AccessId);
		parameters.put("SignatureMethod","HMAC-SHA1");
		parameters.put("Timestamp", Timestamp);
		parameters.put("SignatureVersion","1.0");
		parameters.put("SignatureNonce",SignatureNonce);
		String Signature= AliSignUtils.getSignature(parameters,AccessKey);
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://vod.cn-shanghai.aliyuncs.com" +
				"?Action=DeleteImage" +
				"&DeleteImageType=ImageURL" +
				"&ImageURLs=" + ImageURLs +
				"&Format=JSON" +
				"&Version=2017-03-21" +
				"&AccessKeyId=" + AccessId +
				"&Signature=" + Signature +
				"&SignatureMethod=HMAC-SHA1" +
				"&Timestamp=" + Timestamp +
				"&SignatureVersion=1.0" +
				"&SignatureNonce=" + SignatureNonce);
		try{
			httpClient.execute(httpGet);
//			HttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			String temp= EntityUtils.toString(entity,"UTF-8");
//			System.err.println(temp);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();//释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void deleteAliVideoImg(String ImageIds,String AccessId,String AccessKey){
		String SignatureNonce= UuidUtil.get32UUID();
		String Timestamp= AliSignUtils.getUTCTimeStr();
		Map parameters = new HashMap();
		parameters.put("Action","DeleteImage");
		parameters.put("DeleteImageType","ImageId");
		parameters.put("ImageIds",ImageIds);

		parameters.put("Format","JSON");
		parameters.put("Version","2017-03-21");
		parameters.put("AccessKeyId",AccessId);
		parameters.put("SignatureMethod","HMAC-SHA1");
		parameters.put("Timestamp", Timestamp);
		parameters.put("SignatureVersion","1.0");
		parameters.put("SignatureNonce",SignatureNonce);
		String Signature= AliSignUtils.getSignature(parameters,AccessKey);
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://vod.cn-shanghai.aliyuncs.com" +
				"?Action=DeleteImage" +
				"&DeleteImageType=ImageId" +
				"&ImageIds=" + ImageIds +
				"&Format=JSON" +
				"&Version=2017-03-21" +
				"&AccessKeyId=" + AccessId +
				"&Signature=" + Signature +
				"&SignatureMethod=HMAC-SHA1" +
				"&Timestamp=" + Timestamp +
				"&SignatureVersion=1.0" +
				"&SignatureNonce=" + SignatureNonce);
		try{
			httpClient.execute(httpGet);
//			HttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			String temp= EntityUtils.toString(entity,"UTF-8");
//			System.err.println(temp);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();//释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void deleteAliVideo(String VideoIds,String AccessId,String AccessKey){
		String SignatureNonce= UuidUtil.get32UUID();
		String Timestamp= AliSignUtils.getUTCTimeStr();
		Map parameters = new HashMap();
		parameters.put("Action","DeleteImage");
		parameters.put("VideoIds",VideoIds);

		parameters.put("Format","JSON");
		parameters.put("Version","2017-03-21");
		parameters.put("AccessKeyId",AccessId);
		parameters.put("SignatureMethod","HMAC-SHA1");
		parameters.put("Timestamp", Timestamp);
		parameters.put("SignatureVersion","1.0");
		parameters.put("SignatureNonce",SignatureNonce);
		String Signature= AliSignUtils.getSignature(parameters,AccessKey);
		CloseableHttpClient httpClient= HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://vod.cn-shanghai.aliyuncs.com" +
				"?Action=DeleteImage" +
				"&VideoIds=" + VideoIds +
				"&Format=JSON" +
				"&Version=2017-03-21" +
				"&AccessKeyId=" + AccessId +
				"&Signature=" + Signature +
				"&SignatureMethod=HMAC-SHA1" +
				"&Timestamp=" + Timestamp +
				"&SignatureVersion=1.0" +
				"&SignatureNonce=" + SignatureNonce);
		try{
			httpClient.execute(httpGet);
//			HttpResponse response = httpClient.execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			String temp= EntityUtils.toString(entity,"UTF-8");
//			System.err.println(temp);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();//释放资源
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取访问的域名
	 * @return
	 */
	public static String getDomain(){
		return (((HttpServletRequest)getRequest_Response_Session()[0]).getServerName()+":"+((HttpServletRequest)getRequest_Response_Session()[0]).getServerPort());
	}
	/**
	 * 获取带http或https等开头的完整访问的域名
	 * @return
	 */
	public static String getHttpDomain(){
		StringBuffer url=((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURL();
		return url.delete(url.length() - ((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURI().length(), url.length()).append("/").toString();
	}
	/**
	 * 获取Ip地址
	 * @return
	 */
	public static String getIpAdrress() {
		HttpServletRequest request= (HttpServletRequest) getRequest_Response_Session()[0];
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if(index != -1){
				return XFor.substring(0,index);
			}else{
				return XFor;
			}
		}
		XFor = Xip;
		if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	/**
	 * 辅助方法:判断字符串是否为空字符串或空
	 * @param string
	 * @return
	 */
	public static boolean isNull(Object string){
		return string==null||"".equals(string.toString().trim())||"null".equals(string.toString().trim());
	}
	/**
	 * 判断map里面是否有这个key,并且这key是否不为null,两者有一个否,就返回false
	 * @param m
	 * @param k
	 * @return
	 */
	public static boolean mapGetKeyNotEmpty(Map<String, Object> m,String k){
		return m.containsKey(k)&&m.get(k)!=null&&(m.get(k) instanceof String?m.get(k).toString().trim()!=""||m.get(k).toString().trim()!="null":true);
	}
	/**
	 * 辅助方法:判断集合是否为空
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean listIsNull(List<T>list){return(list==null||list.isEmpty()||list.size()==0||(list.size()==1&&list.get(0)==null));}
	/**
	 * HttpServletRequest从上下文中获取,HttpServletResponse和HttpSession从HttpServletRequest获取
	 * @return Object{HttpServletRequest,HttpServletResponse,HttpSession}
	 */
	public static final Object[] getRequest_Response_Session(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		List<Object>req_res_session=new ArrayList<>();
		if(sra!=null){
			req_res_session.add(sra.getRequest());
			ServletWebRequest servletWebRequest = new ServletWebRequest((HttpServletRequest)req_res_session.get(0));
			req_res_session.add(servletWebRequest.getResponse());
			req_res_session.add(((HttpServletRequest)req_res_session.get(0)).getSession());
			return req_res_session.toArray();
		}else{
			return null;
		}
	}

	/**
	 * 根据需要排序的字段数字,按顺序降序排列
	 * @param list
	 * @param keys
	 * @return
	 */
	public static final List<Map<String, Object>> ListMapOrderByMapKeyDesc(List<Map<String, Object>> list,final String [] keys){
	    Collections.sort(list,new Comparator<Map>() {
	          public int compare(Map o1, Map o2) {
	               return recursion(o1, o2, 0);
	          }
	          private int recursion(Map o1, Map o2, int i) {
	               if (o1.containsKey(keys[i]) && o2.containsKey(keys[i])) {
	                     Object value1 = o1.get(keys[i]);
	                     Object value2 = o2.get(keys[i]);
	                     if (value1 == null && value2 == null) {
	                          if ((i+1) < keys.length) {
	                                int recursion = recursion(o1, o2, i+1);
	                                return recursion;
	                          }else{
	                                return 0;
	                          }
	                     }else if(value1 == null && value2 != null){
	                          return 1;
	                     }else if(value1 != null && value2 == null){
	                          return -1;
	                     }else{
	                          if (value1.equals(value2)) {
	                                if ((i+1) < keys.length) {
	                                     return recursion(o1, o2, i+1);
	                                }else{
	                                     return 0;
	                                }
	                          }else{
	                                if (value1 instanceof String && value2 instanceof String) {
	                                     return value2.toString().compareTo(value1.toString());
	                                }else if(value1 instanceof Timestamp && value2 instanceof Timestamp){
	                                	return ((Timestamp)(value2)).compareTo(new Date(((Timestamp)(value1)).getTime()));
	                                }else{
	                                     return new BigDecimal(value2.toString()).compareTo(new BigDecimal(value1.toString()));
	                                }
	                          }
	                     }
	               }else{
	                     System.out.println(" ** The current map do not containskey : " + keys[i] + ",or The value of key is null **");
	                     return 0;
	               }
	          }
	    });
	    return list;
	}

	/**
	 * 根据keys数组来移除map里对应的key和value
	 * @param map
	 * @param keys
	 */
	public static  void removeMapParmeByKey(Map map,String[]keys){
		for (String key : keys) {
			if(map.containsKey(key))map.remove(key);
		}
	}
	/**
	 * 随机生成指定位数验证码
	 * @return
	 */
	public static String getRandomNum(int count){
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for(int i=0;i<count;i++){
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num)+""), "");
		}
		return sb.toString();
	}
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now;
		now = new Date();
		Date date=new Date();
        try {
            date=df.parse(StrDate);
        } catch (ParseException e) {
            return "未知";
        }
        long times = now.getTime()-date.getTime();
		long day  =  times/(24*60*60*1000);
		long hour = (times/(60*60*1000)-day*24);
		long min  = ((times/(60*1000))-day*24*60-hour*60);
		long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

		StringBuffer sb = new StringBuffer();
		//sb.append("发表于：");
		if(day>0 ){
			sb.append(day+"天前");
		}else if(hour>0 ){
			sb.append(hour+"小时前");
		} else if(min>0){
			sb.append(min+"分钟前");
		} else{
			sb.append(sec+"秒前");
		}
		resultTimes = sb.toString();
		return resultTimes;
	}

	/**
	 * 将数字转换成字符串,并且处理10000开始后面四位数替换成字符W
	 * @param i
	 * @param substring_point_after_length 小数点后保留位数,最多支持4位,超过无效
	 * @return
	 */
	public static String IntegerToString(Integer i,int substring_point_after_length){
		if(isNull(i)){
			return "0";
		}else if(i>9999){
			return String.valueOf(i).substring(0,String.valueOf(i).length()-4)+(substring_point_after_length>0&&substring_point_after_length<5?("."+String.valueOf(i).substring(String.valueOf(i).length()-4,4+substring_point_after_length)):"")+"W";
		}else{
			return String.valueOf(i);
		}
	}
	/**
	 * XML格式字符串转换为Map
	 *
	 * @param strXML XML字符串
	 * @return XML数据转换后的Map
	 * @throws Exception
	 */
	public static Map<String, Object> xmlToMap(String strXML) throws Exception {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(),  element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception ex) {
			getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
			throw ex;
		}

	}
	/**
	 * 日志
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}

	/**
	 * 输入参数,返回一个String,用于判断数据库字段类型为字符串的字段值,为数字的不要用这个方法:</br>
	 * xx is not null and xx<>'' 多个中间用 and 连接
	 * @param arg 需要排除空值和null的字段名
	 * @return
	 */
	public static String notEmptySQL(String ...arg){
		for (int i = 0; i < arg.length; i++) {
			arg[i]=(" "+arg[i]+" is not null and "+arg[i]+"<>'' ");
		}
		return org.apache.commons.lang3.StringUtils.join(arg," and ");
	}

	/**
	 * @param after_day 多少天之后
	 * @return
	 */
	public static Date getFutrueTime(int after_day){
		Date now = new Date();
		Long time =after_day * 24 * 60 * 60 * 1000L;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
		Long end = now.getTime() + time;
		Date date = new Date(end);
		return date;
	}

	public static String aliPay(String body,String APP_ID,String APP_PRIVATE_KEY,String ALIPAY_PUBLIC_KEY,String indent_num,String earnest,String ali_notify_url,String goods_type) throws AlipayApiException {
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body);
		model.setSubject("黔城交友");
		model.setOutTradeNo(indent_num);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(earnest);
		model.setProductCode("QUICK_MSECURITY_PAY");
		model.setGoodsType(goods_type);
		request.setBizModel(model);
		request.setNotifyUrl(ali_notify_url);
		//这里和普通的接口调用不同，使用的是sdkExecute
		AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		return response.getBody();
//		System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
	}

	public static Map<String,Object>getHaveSignatureMap(String jsapi_ticket,String url){
		if(isNull(jsapi_ticket)||isNull(url))return null;
		Map<String,Object>result=new TreeMap<>();
		String noncestr=UuidUtil.get32UUID().substring(0,16),timestamp=String.valueOf(System.currentTimeMillis()).substring(0,10);
		result.put("jsapi_ticket",jsapi_ticket);
		result.put("noncestr",noncestr);
		result.put("timestamp",timestamp);
		result.put("url",url);
		String parame=("jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url);
		result.put("signature",new SHA1().getDigestOfString(parame.getBytes()).toLowerCase());
		return result;
	}

	public static Map<String,Object>getUrlRequestParame(){
		Map<String,Object>result=new HashMap<>();
		Map<String,Object>parameMap=new HashMap<>();
		HttpServletRequest request=((HttpServletRequest)getRequest_Response_Session()[0]);
		StringBuffer buffer=new StringBuffer("?");
		List<String>parame=new ArrayList<>();
		Enumeration enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName=(String)enu.nextElement();
			parameMap.put(paraName,request.getParameter(paraName));
			parame.add((paraName+"="+request.getParameter(paraName)));
		}
		result.put("parameMap",parameMap);
		String urlParame=StringUtils.join(parame,"&");
		result.put("parameString",Tool.isNull(urlParame)?"":("?"+urlParame));
		return result;
	}

	public static void putWX_config(Model model, ISettingService settingService) {
		Setting setting=settingService.selectById(1);
		Map<String,Object> wx_config=getHaveSignatureMap(setting.getWechatTicket(),("http://"+((HttpServletRequest)getRequest_Response_Session()[0]).getServerName()+((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURI()+getUrlRequestParame().get("parameString").toString()));
		wx_config.put("appid",setting.getWechatAppId());
		System.err.println("wx_config:********************************************************************************");
		System.err.println(wx_config);
		model.addAttribute("wx_config",wx_config);
	}


}
