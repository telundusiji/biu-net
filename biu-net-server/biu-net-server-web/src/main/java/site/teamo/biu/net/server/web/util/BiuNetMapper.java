package site.teamo.biu.net.server.web.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BiuNetMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
