package site.teamo.biu.net.common.util;

/**
 * @author 爱做梦的锤子
 * @create 2020/11/27
 */

/**
 * 二元元组
 * @param <T1>
 * @param <T2>
 */
public class Tuple2<T1, T2> {

    public final T1 _1;
    public final T2 _2;

    private Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    public static <T1, T2> Tuple2<T1,T2> of(T1 t1, T2 t2) {
        return new Tuple2(t1, t2);
    }
}
