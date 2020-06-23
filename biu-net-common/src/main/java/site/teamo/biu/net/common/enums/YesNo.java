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


    YesNo(String name, int type) {
        this.name = name;
        this.type = type;
    }
}
