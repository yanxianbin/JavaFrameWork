package com.mybatisfilter;

import java.util.List;
import java.util.Map;

/**
 * @Classname DataScopeAuthService
 * @Description 获取数据权限的Service
 * @Date 2020/5/28 17:35
 * @Created by 125937
 */
public interface DataScopeAuthService {

    Map<String, List<Object>> getDataRange(DataRangeRequest request);
}
