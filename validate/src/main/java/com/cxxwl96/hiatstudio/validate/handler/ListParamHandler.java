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

package com.cxxwl96.hiatstudio.validate.handler;

import com.cxxwl96.hiatstudio.validate.ArgumentValidatorHandler;
import com.cxxwl96.hiatstudio.validate.ValidationChain;
import com.cxxwl96.hiatstudio.validate.annotations.ListParam;
import com.cxxwl96.hiatstudio.validate.metadata.ElementMetadata;
import com.cxxwl96.hiatstudio.validate.metadata.ValidationMetadata;

import java.lang.reflect.Parameter;

/**
 * 处理器：@ListParam注解校验处理器
 *
 * @author cxxwl96
 * @since 2023/3/3 17:46
 */
public class ListParamHandler implements ArgumentValidatorHandler<ListParam> {
    private ListParam listParam;

    /**
     * 初始化方法
     *
     * @param annotation 注解
     */
    @Override
    public void initialize(ListParam annotation) {
        listParam = annotation;
    }

    /**
     * 参数校验处理
     *
     * @param metadata 校验元数据
     * @param chain 校验链
     * @param element 方法参数或类字段的元数据
     * @return 校验通过参数的值
     * @throws Exception 参数校验失败异常
     */
    @Override
    public Object handle(ValidationMetadata metadata, ValidationChain chain, ElementMetadata element) throws Exception {
        if (!element.onParameter()) {
            throw new IllegalArgumentException("ListParam supports only method parameters.");
        }
        // 拦截下一个校验处理器
        chain.intercept();
        // 校验个数，配置了参数长度并且不满足个数相等则校验失败
        constraintSize(listParam.size(), metadata.getParamValues().size());
        // 校验方法参数上的hibernate-validator的校验注解
        String paramName = element.getName();
        Object paramValue = metadata.getParamValues();
        constraintHibernateValidateAnnotations(element.getParameterOrField(Parameter.class), paramName, paramValue);
        return metadata.getParamValues();
    }
}
