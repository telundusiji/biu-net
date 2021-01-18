package site.teamo.biu.net.common.message;

/**
 * @author 爱做梦的锤子
 * @create 2021/1/13
 */
public class UnKnown {
    private final String data = "unknown data";

    private static final UnKnown UN_KNOWN = new UnKnown();

    public static BiuNetMessage<UnKnown> get() {
        return new BiuNetMessage<UnKnown>()
                .setType(MessageType.UN_KNOWN)
                .setContent(UN_KNOWN);
    }
}
