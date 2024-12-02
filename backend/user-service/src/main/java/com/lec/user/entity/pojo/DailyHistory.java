package com.lec.user.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DailyHistory {
    private Long id;
    private Integer week;
    private Integer Mon;
    private Integer Tue;
    private Integer Wed;
    private Integer Thu;
    private Integer Fri;
    private Integer Sat;
    private Integer Sun;

    public DailyHistory setTime(int time,String day,DailyHistory dailyHistory) {
        if (day.equals("星期一")) {
            dailyHistory.setMon(dailyHistory.getMon() + time);
        } else if (day.equals("星期二")) {
            dailyHistory.setTue(dailyHistory.getTue() + time);
        } else if (day.equals("星期三")) {
            dailyHistory.setWed(dailyHistory.getWed() + time);
        } else if (day.equals("星期四")) {
            dailyHistory.setThu(dailyHistory.getThu() + time);
        } else if (day.equals("星期五")) {
            dailyHistory.setFri(dailyHistory.getFri() + time);
        } else if (day.equals("星期六")) {
            dailyHistory.setSat(dailyHistory.getSat() + time);
        } else {
            dailyHistory.setSun(dailyHistory.getSun() + time);
        }
        return dailyHistory;
    }
}
