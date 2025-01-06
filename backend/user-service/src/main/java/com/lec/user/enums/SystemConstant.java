package com.lec.user.enums;

public class SystemConstant {


    /**
     * 更换redis服务器之后要先将对应的卡池数据初始化，不能通过命令行进行输入，而是通过redisTemplate进行输入，不然进行数据读取的时候会产生JSON格式解析错误
     */
    //常驻
    public static String angel="天使卡";//4%
    public static String dev="恶魔卡";
    public static String add="加时卡";//24%
    public static String desc="减时卡";


    //8抽保底卡
    public static String sendCard="赠送卡";

    //up池
    public static String bad="黑卡";//1%
    public static String good="福利卡";//1%
    public static String stealCard="偷卡";//10%
    public static String stealOneHour="偷偷卡";//10%

    public static String Redis_card="card:";
    public static String Redis_draw_cards="draw:card:";
    private SystemConstant(){}

    public static String JWTKey = "%$%#DQWs124W@$%!&&*ASD@Q1sqd124!";
    public static String REDIS_LOGIN_USER = "clock:login:";
    public static String Redis_Up="draw:up:";
    public static Integer CLOCKING_STATUS = 1;

    public static Integer CLOCKED_STATUS = 0;

    public static Integer FIRST_GRADE_CLOCK_TARGET = 1200;
    public static Integer SECOND_GRADE_CLOCK_TARGET = 1920;

    public static int HASH_INCREMENT = 0x61c88647;

    public static String REDIS_CLOCK_IPV4="ipv4:";

    public static String REDIS_RECORD_IPV4="record:ipv4:";
    public static String REDIS_WEEK = "clock:week";

    public static String OSS_IP = "154.44.25.122:9000";

    public static String OSS_URL = "https://kilo-webtest.oss-cn-beijing.aliyuncs.com/";

    public static String default_URL="https://kilo-webtest.oss-cn-beijing.aliyuncs.com/06067cf4-2175-4a70-b719-26e12df69a42.jpg";

    public static String signature="时间如梭，每一次的打卡都是自律的一步，坚守目标的承诺。记录生活，珍惜时光。";

    public static String adminEmail = "3551147139@qq.com";
}
