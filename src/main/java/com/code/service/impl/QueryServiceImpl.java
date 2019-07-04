package com.code.service.impl;

import com.code.anno.CodeService;
import com.code.service.QueryService;

/**
 * @author liuzhuang
 * @date 2018/12/13 9:48
 */
@CodeService("queryService")
public class QueryServiceImpl implements QueryService {
    @Override
    public String queryMethod(String name, int age) {
        return "{'name':" + name + ";'age':" + age + "}";
    }
}
