package com.wl.comom;


import com.wl.cfwl.R;

import java.util.ArrayList;
import java.util.List;

public class CommonEnum {

    public enum zdsum {


       /* 阿克苏(R.raw.aks), 阿勒泰(R.raw.alt), 博乐市(R.raw.bls), 昌吉回族自治州(R.raw.cjhzzzq), 和田(R.raw.ht),
        河北山西甘肃(R.raw.hbsxgs), 湖北(R.raw.hb), 喀什(R.raw.ks), 库车市(R.raw.kcs), 库尔勒市(R.raw.kels), 内蒙古东北(R.raw.nmgdb), 全部(R.raw.all), 上海测试(R.raw.shcs), 石河子(R.raw.shz), 塔城(R.raw.tc), 伊犁(R.raw.yl);
*/
      /*  zdsum(int value) {
            this.value = value;
        }

        private int value;

        public int getValue() {
            return this.value;
        }*/

    }
    public  static List<String> messageSum = new ArrayList<>();
    public static  List<String> setALL()
    {
        messageSum = new ArrayList<>();
        messageSum.add("阿克苏");
        messageSum.add("阿勒泰");
        messageSum.add("博乐市");
        messageSum.add("昌吉回族自治州");
        messageSum.add("和田");
        messageSum.add("河北&山西&甘肃");
        messageSum.add("湖北");
        messageSum.add("喀什");
        messageSum.add("库车市");
        messageSum.add("库尔勒市");
        messageSum.add("内蒙古&东北");
        messageSum.add("上海&测试");
        messageSum.add("石河子");
        messageSum.add("塔城");
        messageSum.add("伊犁");

        return messageSum;

    }
   /* public static int getID(String name) {
        switch (name) {
            case "阿克苏":
                return (R.raw.aks);
            case "阿勒泰":
                return (R.raw.alt);
            case "博乐市":
                return (R.raw.bls);
            case "昌吉回族自治州":
                return (R.raw.cjhzzzq);
            case "和田":
                return (R.raw.ht);
            case "河北&山西&甘肃":
                return (R.raw.hbsxgs);
            case "湖北":
                return (R.raw.hb);
            case "喀什":
                return (R.raw.ks);
            case "库车市":
                return (R.raw.kcs);
            case "库尔勒市":
                return (R.raw.kels);
            case "内蒙古&东北":
                return (R.raw.nmgdb);
            case "全部":
                return (R.raw.all);
            case "上海&测试":
                return (R.raw.shcs);
            case "石河子":
                return (R.raw.shz);
            case "塔城":
                return (R.raw.tc);
            case "伊犁":
                return (R.raw.yl);
            case "南疆":
                return (R.raw.nj);
            case "北疆":
                return (R.raw.bj);
        }
        return 0;
    }*/
}
