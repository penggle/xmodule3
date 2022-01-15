package com.penglecode.xmodule.samples.common.test;

import com.penglecode.xmodule.common.model.OrderBy;
import com.penglecode.xmodule.common.model.Page;
import com.penglecode.xmodule.common.support.BeanValidator;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.boot.Sample1Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/15 15:00
 */
@SpringBootTest(classes=Sample1Application.class, webEnvironment=WebEnvironment.NONE)
public class ValidatorTest {

    @Test
    public void test1(){
        Page pager = Page.ofDefault();
        pager.setPageIndex(0);
        pager.setPageSize(0);
        pager.setOrderBys(Collections.singletonList(OrderBy.desc("abc")));
        System.out.println(JsonUtils.object2Json(pager));
        BeanValidator.validateBean(pager, Page::getPageIndex, Page::getPageSize, Page::getOrderBys);
    }

    @Test
    public void test2(){
        Map<String,Object> pager = new HashMap<>();
        pager.put("orderBys", Collections.singletonList(OrderBy.desc("")));
        System.out.println(JsonUtils.object2Json(pager));
        BeanValidator.validateMap(pager, Page::getOrderBys);
    }

    @Test
    public void test3(){
        Page pager = Page.ofDefault();
        pager.setOrderBys(Collections.singletonList(OrderBy.desc("")));
        System.out.println(JsonUtils.object2Json(pager));
        int total = 100000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < total; i++) {
            try {
                BeanValidator.validateProperty(pager.getOrderBys(), Page::getOrderBys);
            } catch (Exception e) {
                //ignored
            }
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) * 1.0 / total);
    }

    @Test
    public void test4(){
        Map<String,Object> pager = new HashMap<>();
        pager.put("orderBys", Collections.singletonList(OrderBy.desc("")));
        System.out.println(JsonUtils.object2Json(pager));
        int total = 100000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < total; i++) {
            try {
                BeanValidator.validateMap(pager, Page::getOrderBys);
            } catch (Exception e) {
                //ignored
            }
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) * 1.0 / total);
    }

}
