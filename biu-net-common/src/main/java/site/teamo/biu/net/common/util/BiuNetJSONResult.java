package site.teamo.biu.net.common.util;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.teamo.biu.net.common.exception.ResponseCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiuNetJSONResult<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public static BiuNetJSONResult ok() {
        return BiuNetJSONResult.builder()
                .code(ResponseCode.OK)
                .build();
    }

    public static BiuNetJSONResult ok(Object data) {
        return BiuNetJSONResult.builder()
                .code(ResponseCode.OK)
                .data(data)
                .build();
    }

    public static <T> BiuNetJSONResult<T> okType(T data) {
        return BiuNetJSONResult.<T>builder()
                .code(ResponseCode.OK)
                .data(data)
                .build();
    }

    public static BiuNetJSONResult error(int code, String msg) {
        return BiuNetJSONResult.builder()
                .code(code)
                .msg(msg)
                .build();
    }

    public static BiuNetJSONResult error(int code, Object data) {
        return BiuNetJSONResult.builder()
                .code(code)
                .data(data)
                .build();
    }

    public static <E> BiuNetJSONResult<PageData<E>> ok(PageInfo<E> pageInfo) {
        return BiuNetJSONResult.PageData.<E>builder()
                .list(pageInfo.getList())
                .count(pageInfo.getTotal())
                .pageNo(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .build().buildJSONResult();
    }

    public static OkMap okMap() {
        return new OkMap();
    }

    public static class OkMap {
        private HashMap<String, Object> data = new HashMap<>();

        public OkMap put(String key, Object value) {
            data.put(key, value);
            return this;
        }

        public BiuNetJSONResult<Map<String, Object>> build() {
            return BiuNetJSONResult.ok(data);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageData<E> {
        private long count;
        private int pageSize;
        private int pageNo;
        private List<E> list;

        public BiuNetJSONResult<PageData<E>> buildJSONResult() {
            return BiuNetJSONResult.<PageData<E>>builder()
                    .code(ResponseCode.OK)
                    .data(this)
                    .build();
        }
    }
}
