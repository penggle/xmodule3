package com.penglecode.xmodule.samples.common.support.test;

import com.penglecode.xmodule.common.support.BeanCopier;
import com.penglecode.xmodule.common.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/16 13:55
 */
public class BeanCopierTest {

    @Test
    public void simpleTest1() {
        AccountDO account = new AccountDO(1L, "6224718239212032", "1", 127.00, Collections.singleton("poor"));
        CustomerDO source = new CustomerDO(111L, "阿三", "男", 33, "1989-03-21");
        source.setAccount(account);
        source.setElements(Collections.singleton(account));
        System.out.println("source ==> " + JsonUtils.object2Json(source));
        CustomerDTO target = BeanCopier.copy(source, CustomerDTO::new);
        System.out.println("target ==> " + JsonUtils.object2Json(target));
    }

    @Test
    public void simpleTest2() {
        AccountDO account = new AccountDO(1L, "6224718239212032", "1", 127.00, Collections.singleton("poor"));
        CustomerDO source = new CustomerDO(111L, "阿三", "男", 33, "1989-03-21");
        source.setAccount(account);
        source.setElements(Collections.singleton(account));
        int total = 100000;
        long start = System.currentTimeMillis();
        CustomerDTO target = BeanCopier.copy(source, CustomerDTO::new);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + ", " + (end - start) * 1.0 / total);
    }

}
