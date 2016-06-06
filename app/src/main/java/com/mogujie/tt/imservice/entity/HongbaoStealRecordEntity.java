package com.mogujie.tt.imservice.entity;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * 红包领取详情里的实体类
 * @author : gy on 16-5-24
 */
public class HongbaoStealRecordEntity {
    private String portraitPath;
    private String hongbaoStealer;
    private long hongbaoNum;

    public HongbaoStealRecordEntity(String portraitPath, String hongbaoStealer, long hongbaoNum) {
        this.portraitPath = portraitPath;
        this.hongbaoStealer = hongbaoStealer;
        this.hongbaoNum = hongbaoNum;
    }

    public long getHongbaoNum() {
        return hongbaoNum;
    }

    public void setHongbaoNum(long hongbaoNum) {
        this.hongbaoNum = hongbaoNum;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getHongbaoStealer() {
        return hongbaoStealer;
    }

    public void setHongbaoStealer(String hongbaoStealer) {
        this.hongbaoStealer = hongbaoStealer;
    }
}
