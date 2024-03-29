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

package com.cxxwl96.hiatstudio.validate;

import com.cxxwl96.hiatstudio.validate.metadata.ValidationMetadata;

import java.lang.annotation.Annotation;

/**
 * 方法校验接口
 *
 * @author cxxwl96
 * @since 2023/3/3 14:09
 */
public interface MethodValidatorHandler<A extends Annotation> extends Initializable<A>, Constraintable {
    /**
     * 方法校验处理
     *
     * @param metadata 校验元数据
     * @param chain 校验链
     * @throws Exception 参数校验失败异常
     */
    void handle(ValidationMetadata metadata, ValidationChain chain) throws Exception;
}
