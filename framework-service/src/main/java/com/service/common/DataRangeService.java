package com.service.common;

import com.mybatisfilter.DataScopeAuthService;
import com.mybatisfilter.DataRangeRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname DataRangeService
 * @Description 获取权限
 * @Date 2020/5/28 18:11
 * @Created by 125937
 */
@Service
public class DataRangeService implements DataScopeAuthService {
    @Override
    public Map<String, List<Object>> getDataRange(DataRangeRequest rangeRequest) {
        Map<String, List<Object>> map=new HashMap<>();
        map.put("accounts", Arrays.asList("1","2","3"));
        return map;
    }
}
