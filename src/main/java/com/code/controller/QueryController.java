package com.code.controller;

import com.code.anno.CodeAutowird;
import com.code.anno.CodeRequestMapping;
import com.code.anno.CodeController;
import com.code.service.QueryService;

import javax.naming.Name;

/**
 * @author liuzhuang
 * @date 2018/12/13 9:50
 */
@CodeController
@CodeRequestMapping("/code")
public class QueryController {
    @CodeAutowird("queryService")
    private QueryService queryService;

    @CodeRequestMapping("/query")
    public String queryParam(String Name, int age){

        return queryService.queryMethod(Name,age);
    }
}
