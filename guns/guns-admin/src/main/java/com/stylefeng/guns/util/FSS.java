package com.stylefeng.guns.util;

public class FSS {
	public static final String timeFormatRegx="((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))";
	public static final String timeFormatYMD="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

	/**
	 * 后台接口文档访问效果图的url一级目录名
	 */
	public  static  final String qcgs="/static/qcgs/";
	/**
	 * 后台接口文档访问效果图的url一级目录名映射到硬盘的路径
	 */
	public static final String xgt_path="/lijun/xccb/xgt/";
	/**
	 * 文件上传路径
	 */
	public static final String FILEPATHFILE_OFFLINE = "D:/qcgs/";			//文件上传路径
	public static final String FILEPATHFILE_ONLINE = "/arron/qcgu/";			//文件上传路径


	public static final String FILE_UPLOAD_PATH_LOC="D:/项目测试路径/qcgs/";
	public static final String FILE_UPLOAD_PATH_ONLINE="/root/qcgs/";

	public static final String MD5_yan="b64dd805-00bf-6b74-ed51-920828ef8f53";
	public static final String MD5_yan_IOS="b64dd805-00bf-6b74-ed51-9208";
}
