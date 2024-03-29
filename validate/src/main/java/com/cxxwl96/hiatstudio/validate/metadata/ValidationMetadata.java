/*
 * Copyright (c) 2021-2023, jad (cxxwl96@sina.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cxxwl96.hiatstudio.validate.metadata;

import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import lombok.Getter;

/**
 * 参数校验数据元
 *
 * @author cxxwl96
 * @since 2023/2/28 19:55
 */
@Getter
public class ValidationMetadata {
    // 参数名工具，Spring的参数名工具
    private static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    // 执行的功能接口
    private final Method runMethod;

    // 参数名
    private final List<String> paramNames;

    // 功能接口的参数
    private final List<String> paramValues;

    // 功能接口参数中可能存在通过@ReturnData注入返回结果
    private final List<String> returnData;

    public ValidationMetadata(Method runMethod, List<String> paramValues, List<String> returnData) {
        Assert.notNull(runMethod, "runMethod cannot be null.");
        Assert.notNull(paramValues, "paramValues cannot be null.");
        Assert.notNull(returnData, "returnData cannot be null.");
        this.runMethod = runMethod;
        // 使用Spring工具获取参数名
        this.paramNames = CollUtil.newArrayList(DISCOVERER.getParameterNames(runMethod));
        this.paramValues = paramValues;
        this.returnData = returnData;
    }
}
