package com.stylefeng.guns.util;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
public enum excelEnum {
    create_time("创建时间","create_time"),
    id("会员编号","id"),
    name("昵称","name"),
    area_name("省市","area_name"),
    introducer("介绍人","introducer"),
    assistant("归属星厨助理（姓名+手机号）","assistant"),
    real_name("会员姓名","real_name"),
    sex("性别","sex"),
    age("年龄","age"),
    brithdate("出生日期","brithdate"),
    id_card_num("身份证号码","id_card_num"),
    phone("联系电话","phone"),
    wechat("微信号","wechat"),
    address("联系地址","address"),
    working_time("厨师从业时长","working_time"),
    is_usertarget("是否加入星厨俱乐部","is_usertarget"),
    lv("星厨评级","lv"),
    works_num("发布作品数","works_num"),
    activity_num("发起活动次数","activity_num"),
    friend_circle_num("厨友圈动态数","friend_circle_num"),
    fabulous_num("获赞","fabulous_num"),
    follow_num("粉丝","follow_num"),
    master_disciple_num("师/徒","master_disciple_num"),
    login_num("登陆次数","login_num"),
    last_login_time("最近一次登陆时间","last_login_time"),
    restaurant_name("目前供职餐厅/酒店名称","restaurant_name"),
    job_title("从事职位","job_title"),
    compensation("薪资","compensation"),
    restaurant_address("餐厅地址","restaurant_address"),
    banquet_hall_num("大厅宴席数","banquet_hall_num"),
    restaurant_scale("餐厅面积规模/餐厅星级","restaurant_scale"),
    dish_styles("擅长菜系","dish_styles"),
    dishes("擅长菜品","dishes"),
    representative_dish("自主研发代表菜品","representative_dish"),
    exclusive("个人绝活","exclusive"),
    caste("社会任职及头衔","caste"),

    eontime("时间段","eontime"),
    employer("任职单位","employer"),
    duty("担任职务","duty"),
    payroll("薪资","payroll"),

    certificate("证书名称","certificate"),
    grade("认证等级","grade"),

    prize("获得奖项","prize"),
    skillGame("技能比赛/活动名称","skillGame"),
    sponsor("主办单位","sponsor"),

    experience("工作经历", Arrays.asList(new excelEnum[]{eontime,employer,duty,payroll}),"experience"),
    skill("获得技能证书及认证等级",Arrays.asList(new excelEnum[]{certificate,grade}),"skill"),
    awards("曾参加的技能比赛和获得奖项",Arrays.asList(new excelEnum[]{prize,skillGame,sponsor}),"awards");

    private String column_name;
    private List<excelEnum>column_names;
    private String column_enname;
    excelEnum(String column_name,String column_enname) {
        this.column_name = column_name;
        this.column_enname=column_enname;
    }

    excelEnum(String column_name, List<excelEnum> column_names, String column_enname) {
        this.column_name = column_name;
        this.column_names = column_names;
        this.column_enname=column_enname;
    }

    excelEnum(List<excelEnum> column_names) {
        this.column_names = column_names;
    }
    public List<String>getSecondColumnNames(excelEnum e){
        List<String>names=new ArrayList<>();
        for (excelEnum columnName : e.getColumn_names())names.add(columnName.getColumn_name());
        return names;
    }
}
