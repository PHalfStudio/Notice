package pro.phalfstudio.notice.bean;

import java.util.Map;

public class NetBackOneNotice {
    private int code;
    private Data data;
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
        private int todayCount;
        private int allCount;
        private LatestNotice latestNotice;

        public int getTodayCount() {
            return todayCount;
        }

        public void setTodayCount(int todayCount) {
            this.todayCount = todayCount;
        }

        public int getAllCount() {
            return allCount;
        }

        public void setAllCount(int allCount) {
            this.allCount = allCount;
        }

        public LatestNotice getLatestNotice() {
            return latestNotice;
        }

        public void setLatestNotice(LatestNotice latestNotice) {
            this.latestNotice = latestNotice;
        }

        public static class LatestNotice {
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
}

