package com.wl.entry;

import java.util.List;

public class ResponseData {

    public class Site {
        public String id;
        public String sitename;
        public String longitude;
        public String latitude;
        public String radiofrequency;
        public String ip;
        public String port;
        public String mountpoint;
        public String passward;
        public String type;
        public long createdate;
        public String orgid;
        public String usetype;
        public String mregion;
        // 添加构造函数、Getter和Setter方法
        // ...
    }


        public int statusCode;
        public String messagecn;
        public String messageen;
        public List<Site> data;


}
