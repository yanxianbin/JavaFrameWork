package com.controller.common;

import com.request.DataRangeRequest;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname DataScopeController
 * @Description TODO
 * @Date 2020/5/28 18:09
 * @Created by 125937
 */
@Controller
public class DataScopeController {


    public Map<String, List<String>> getDataScope(DataRangeRequest rangeRequest){
        return new HashMap<>();
    }

}
