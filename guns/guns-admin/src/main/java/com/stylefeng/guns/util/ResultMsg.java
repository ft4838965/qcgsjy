package com.stylefeng.guns.util;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Enzo Cotter on 2018/7/16.
 */

@ApiModel(value = "返回说明")
public class ResultMsg {
    @ApiModelProperty(value = "成功标识;success:成功;fail:失败")
    private String success;
    @ApiModelProperty(value = "可直接展示给用户看的消息")
    private String message;
    @ApiModelProperty(value = "给前端程序员看的消息")
    private String detail;
    @ApiModelProperty(value = "请参看每个接口Responses里状态码为200的补充说明")
    private Object data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultMsg success(String message, String detail, Object data) {
        ResultMsg resultMsg = new ResultMsg();

        resultMsg.setSuccess("ok");
        resultMsg.setMessage(message);
        resultMsg.setDetail(detail);
        if (data != null)
            resultMsg.setData(data);

        return resultMsg;
    }

    public static ResultMsg fail(String message, String detail, Object data) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setSuccess("fail");
        resultMsg.setMessage(message);
        resultMsg.setDetail(detail);
        if (data != null)
            resultMsg.setData(data);

        return resultMsg;
    }

    public static ResultMsg unSuccess(String message) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setMessage(message);
        return resultMsg;
    }

}
