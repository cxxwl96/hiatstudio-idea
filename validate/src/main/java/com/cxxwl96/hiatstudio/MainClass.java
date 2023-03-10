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

package com.cxxwl96.hiatstudio;

import com.cxxwl96.hiatstudio.validate.CustomValidatorHandler;
import com.cxxwl96.hiatstudio.validate.ValidationBuilder;
import com.cxxwl96.hiatstudio.validate.ValidationChain;
import com.cxxwl96.hiatstudio.validate.ValidationMetadata;
import com.cxxwl96.hiatstudio.validate.ValidationResult;
import com.cxxwl96.hiatstudio.validate.annotations.BasicParam;
import com.cxxwl96.hiatstudio.validate.annotations.BeanParam;
import com.cxxwl96.hiatstudio.validate.annotations.IgnoreField;
import com.cxxwl96.hiatstudio.validate.annotations.JsonParam;
import com.cxxwl96.hiatstudio.validate.annotations.ListParam;
import com.cxxwl96.hiatstudio.validate.annotations.ParamValidator;
import com.cxxwl96.hiatstudio.validate.annotations.ReturnData;
import com.cxxwl96.hiatstudio.validate.custom.In;
import com.cxxwl96.hiatstudio.validate.handler.BasicParamHandler;
import com.cxxwl96.hiatstudio.validate.handler.BeanParamsHandler;
import com.cxxwl96.hiatstudio.validate.handler.JsonParamHandler;
import com.cxxwl96.hiatstudio.validate.handler.ListParamsHandler;
import com.cxxwl96.hiatstudio.validate.handler.ParamValidatorHandler;
import com.cxxwl96.hiatstudio.validate.handler.ReturnDataHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * MainClass
 *
 * @author cxxwl96
 * @since 2023/2/27 20:22
 */
@Slf4j
public class MainClass {
    public static void main(String[] args) {
        // ???????????????
        final Method runMethod = ReflectUtil.getMethodByName(MainClass.class, "myRunMethod");
        final List<String> parameters = CollUtil.newArrayList("cyk", "18", "true", "39793666111", "[\"??????\",\"??????\"]",
            "{\"key1\":\"value1\",\"key2\":[\"value2\",\"value3\"]}");
        final ArrayList<String> returnData = new ArrayList<>();

        // ????????????
        final ValidationResult result = ValidationBuilder.builder(
            new ValidationMetadata(runMethod, parameters, returnData))
            .addMethodValidator(new ParamValidatorHandler())
            .addArgumentValidator(new BasicParamHandler())
            .addArgumentValidator(new JsonParamHandler())
            .addArgumentValidator(new BeanParamsHandler())
            .addArgumentValidator(new ListParamsHandler())
            .addArgumentValidator(new ReturnDataHandler())
            .build()
            .validate();

        if (result.isSuccess()) {
            System.out.println("????????????");
            final String values = Arrays.stream(result.getParamValues())
                .map(item -> item == null ? "null" : item.toString())
                .collect(Collectors.joining(","));
            System.out.println("???????????????" + values);
            // ????????????
            ReflectUtil.invoke(new MainClass(), runMethod, result.getParamValues());
        } else {
            System.err.println(result.getErrorMessage());
        }

    }

    // size: ????????????
    // customValidatorHandler: ????????????????????????
    @ParamValidator(size = 6, customValidatorHandler = MyValidatorHandler.class)
    private void myRunMethod(
        // ???????????????
        @BasicParam(index = 0) @NotBlank String name,
        // ??????????????????????????????@In??????
        @BasicParam(index = 1) @Min(10) @Max(20) @In(values = {11, 12, 13, 18}) int age,
        // ??????boolean
        @BasicParam(index = 2) boolean married,
        // ??????????????????????????????
        @BasicParam(index = 3) @Pattern(regexp = "[1-9][0-9]{4,10}") String qq,
        // ??????JSON????????????????????????
        @JsonParam(index = 4) List<String> addresses,
        // ??????JSON????????????????????????
        @JsonParam(index = 5) JsonObject jsonObj,

        // ??????JavaBean
        @BeanParam(size = 6) BeanParams beanParams,

        // ??????List<String>
        @ListParam List<String> listParams,

        // ????????????
        @ReturnData List<String> returnData) {

        // ?????????..........
        System.out.println();
    }

    @Data
    public static class BeanParams {
        @NotBlank
        private String name;

        @Min(10)
        @Max(20)
        private int age;

        private boolean married;

        @Pattern(regexp = "[1-9][0-9]{4,10}")
        private String qq;

        @IgnoreField
        private String ignoreField;

        // ??????JSON????????????????????????
        @JsonParam(index = 4)
        private List<String> addresses;

        // ??????JSON????????????????????????
        @JsonParam(index = 5)
        private JsonObject jsonObj;

        // setter????????????
        private void setName(String name) {
            this.name = name;
            // ???????????????????????????
        }
    }

    @Data
    public static class JsonObject {
        private String key1;

        private List<String> key2;
    }

    public static class MyValidatorHandler implements CustomValidatorHandler {
        /**
         * ?????????????????????
         *
         * @param params ????????????
         * @param chain ?????????
         * @throws IllegalArgumentException ??????????????????
         */
        @Override
        public void handle(List<String> params, ValidationChain chain) throws IllegalArgumentException {
            chain.intercept();
            if (params.size() != 6) {
                throw new IllegalArgumentException("Invalid params.");
            }
        }
    }
}
