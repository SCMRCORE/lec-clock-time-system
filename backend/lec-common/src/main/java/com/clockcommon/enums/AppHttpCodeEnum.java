package com.clockcommon.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    CODE_FALSE(402, "验证码错误"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    CLOCK_TIMEOUT(405, "打卡时长超过五小时，打卡无效！（咋这么能卷，休息休息~~）"),
    FILE_TYPE_ERROR(406, "文件类型错误"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    CODE_SEND_ERROR(506, "验证码发送失败"),
    USER_NOT_EXIT(507, "Redis不存在该注册用户"),
    OPENFIRGN_ERROR(508, "Openfire请求失败"),
    EMAIL_ERROR(509, "邮箱发送出错");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}