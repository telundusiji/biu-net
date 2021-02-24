package site.teamo.biu.net.common.enums;

/**
 * @author 爱做梦的锤子
 * @create 2020/6/23
 */
public enum YesNo {

    NO("否", 0),
    YES("是", 1);

    public String name;
    public int type;

    public boolean isYes() {
        return type == 1;
    }

    public <T> T isYes(T yes, T no) {
        return type == 1 ? yes : no;
    }

    public boolean isNo() {
        return type == 0;
    }

    public <T> T isNo(T yes, T no) {
        return type == 0 ? yes : no;
    }

    public static YesNo typeOf(Integer type) {
        for (YesNo value : values()) {
            if (value.type == type) return value;
        }
        throw new RuntimeException("There is no matching enumeration type for YesNo");
    }

    YesNo(String name, int type) {
        this.name = name;
        this.type = type;
    }
}
