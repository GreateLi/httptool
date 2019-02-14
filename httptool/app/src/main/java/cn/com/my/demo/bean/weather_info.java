package cn.com.my.demo.bean;

import com.google.gson.annotations.SerializedName;
import com.http.tools.net.common.BasicResponse;

import java.util.List;

public class weather_info extends BasicResponse {

    /**
     * data : {"yesterday":{"date":"10日星期日","high":"高温 -1℃","fx":"西南风","low":"低温 -8℃","fl":"<![CDATA[<3级]]>","type":"多云"},"city":"北京","forecast":[{"date":"11日星期一","high":"高温 3℃","fengli":"<![CDATA[<3级]]>","low":"低温 -6℃","fengxiang":"北风","type":"多云"},{"date":"12日星期二","high":"高温 -3℃","fengli":"<![CDATA[<3级]]>","low":"低温 -8℃","fengxiang":"东风","type":"小雪"},{"date":"13日星期三","high":"高温 1℃","fengli":"<![CDATA[<3级]]>","low":"低温 -7℃","fengxiang":"东北风","type":"多云"},{"date":"14日星期四","high":"高温 -1℃","fengli":"<![CDATA[<3级]]>","low":"低温 -6℃","fengxiang":"南风","type":"阴"},{"date":"15日星期五","high":"高温 3℃","fengli":"<![CDATA[3-4级]]>","low":"低温 -7℃","fengxiang":"西北风","type":"晴"}],"ganmao":"将有一次强降温过程，天气寒冷，极易发生感冒，请特别注意增加衣服保暖防寒。","wendu":"1"}
     * status : 1000
     * desc : OK
     */
    @SerializedName("data")
    private DataEntity data;
    @SerializedName("status")
    private int status;
    @SerializedName("desc")
    private String desc;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DataEntity getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public class DataEntity {
        /**
         * yesterday : {"date":"10日星期日","high":"高温 -1℃","fx":"西南风","low":"低温 -8℃","fl":"<![CDATA[<3级]]>","type":"多云"}
         * city : 北京
         * forecast : [{"date":"11日星期一","high":"高温 3℃","fengli":"<![CDATA[<3级]]>","low":"低温 -6℃","fengxiang":"北风","type":"多云"},{"date":"12日星期二","high":"高温 -3℃","fengli":"<![CDATA[<3级]]>","low":"低温 -8℃","fengxiang":"东风","type":"小雪"},{"date":"13日星期三","high":"高温 1℃","fengli":"<![CDATA[<3级]]>","low":"低温 -7℃","fengxiang":"东北风","type":"多云"},{"date":"14日星期四","high":"高温 -1℃","fengli":"<![CDATA[<3级]]>","low":"低温 -6℃","fengxiang":"南风","type":"阴"},{"date":"15日星期五","high":"高温 3℃","fengli":"<![CDATA[3-4级]]>","low":"低温 -7℃","fengxiang":"西北风","type":"晴"}]
         * ganmao : 将有一次强降温过程，天气寒冷，极易发生感冒，请特别注意增加衣服保暖防寒。
         * wendu : 1
         */
        @SerializedName("yesterday")
        private YesterdayEntity yesterday;
        @SerializedName("city")
        private String city;
        @SerializedName("forecast")
        private List<ForecastEntity> forecast;
        @SerializedName("ganmao")
        private String ganmao;
        @SerializedName("wendu")
        private String wendu;

        public void setYesterday(YesterdayEntity yesterday) {
            this.yesterday = yesterday;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setForecast(List<ForecastEntity> forecast) {
            this.forecast = forecast;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public YesterdayEntity getYesterday() {
            return yesterday;
        }

        public String getCity() {
            return city;
        }

        public List<ForecastEntity> getForecast() {
            return forecast;
        }

        public String getGanmao() {
            return ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public class YesterdayEntity {
            /**
             * date : 10日星期日
             * high : 高温 -1℃
             * fx : 西南风
             * low : 低温 -8℃
             * fl : <![CDATA[<3级]]>
             * type : 多云
             */
            @SerializedName("date")
            private String date;
            @SerializedName("high")
            private String high;
            @SerializedName("fx")
            private String fx;
            @SerializedName("low")
            private String low;
            @SerializedName("fl")
            private String fl;
            @SerializedName("type")
            private String type;

            public void setDate(String date) {
                this.date = date;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public String getHigh() {
                return high;
            }

            public String getFx() {
                return fx;
            }

            public String getLow() {
                return low;
            }

            public String getFl() {
                return fl;
            }

            public String getType() {
                return type;
            }
        }

        public class ForecastEntity {
            /**
             * date : 11日星期一
             * high : 高温 3℃
             * fengli : <![CDATA[<3级]]>
             * low : 低温 -6℃
             * fengxiang : 北风
             * type : 多云
             */
            @SerializedName("date")
            private String date;
            @SerializedName("high")
            private String high;
            @SerializedName("fengli")
            private String fengli;
            @SerializedName("low")
            private String low;
            @SerializedName("fengxiang")
            private String fengxiang;
            @SerializedName("type")
            private String type;

            public void setDate(String date) {
                this.date = date;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public String getHigh() {
                return high;
            }

            public String getFengli() {
                return fengli;
            }

            public String getLow() {
                return low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public String getType() {
                return type;
            }
        }
    }

    @Override
    public String toString() {
        return "weather_info{" +
                "data=" + data +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                '}';
    }
}
