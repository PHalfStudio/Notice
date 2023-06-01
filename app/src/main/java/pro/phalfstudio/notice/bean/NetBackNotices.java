package pro.phalfstudio.notice.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class NetBackNotices {
    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private Data data;

    @SerializedName("msg")
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Data {
        @SerializedName("records")
        private List<Record> records;

        @SerializedName("total")
        private int total;

        @SerializedName("size")
        private int size;

        @SerializedName("current")
        private int current;

        @SerializedName("orders")
        private List<?> orders;

        @SerializedName("optimizeCountSql")
        private boolean optimizeCountSql;

        @SerializedName("searchCount")
        private boolean searchCount;

        @SerializedName("maxLimit")
        private Object maxLimit;

        @SerializedName("countId")
        private Object countId;

        @SerializedName("pages")
        private int pages;

        public List<Record> getRecords() {
            return records;
        }

        public void setRecords(List<Record> records) {
            this.records = records;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public List<?> getOrders() {
            return orders;
        }

        public void setOrders(List<?> orders) {
            this.orders = orders;
        }

        public boolean isOptimizeCountSql() {
            return optimizeCountSql;
        }

        public void setOptimizeCountSql(boolean optimizeCountSql) {
            this.optimizeCountSql = optimizeCountSql;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public Object getMaxLimit() {
            return maxLimit;
        }

        public void setMaxLimit(Object maxLimit) {
            this.maxLimit = maxLimit;
        }

        public Object getCountId() {
            return countId;
        }

        public void setCountId(Object countId) {
            this.countId = countId;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }
    }

    public static class Record {
        private int id;
        private String date;
        private String time;
        private String title;
        private String body;
        private String url;
        private String[] images;
        private String[] append;
        private Map<String,String> appendMap;
        private String[] imagesArray;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String[] getImages() {
            return images;
        }

        public void setImages(String[] images) {
            this.images = images;
        }

        public String[] getAppend() {
            return append;
        }

        public void setAppend(String[] append) {
            this.append = append;
        }

        public Map<String, String> getAppendMap() {
            return appendMap;
        }

        public void setAppendMap(Map<String, String> appendMap) {
            this.appendMap = appendMap;
        }

        public String[] getImagesArray() {
            return imagesArray;
        }

        public void setImagesArray(String[] imagesArray) {
            this.imagesArray = imagesArray;
        }
    }
}
