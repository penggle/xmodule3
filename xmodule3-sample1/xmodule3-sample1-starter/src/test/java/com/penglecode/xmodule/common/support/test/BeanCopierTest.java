package com.penglecode.xmodule.common.support.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.product.api.request.QueryProductResponse;
import com.penglecode.xmodule.samples.product.domain.model.ProductAggregate;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ClassUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/11 20:08
 */
public class BeanCopierTest {

    public static void main(String[] args) {
        //test2();
        //test3();
        //test4();
        test5();
    }

    protected static void test1() {
        String productJson = "{\"productId\":1,\"productName\":\"24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通\",\"productUrl\":\"https://detail.tmall.com/item.htm?id=633658852628\",\"productTags\":\"华为手机 5G mate40pro\",\"productType\":1,\"auditStatus\":1,\"onlineStatus\":1,\"shopId\":111212422,\"remark\":\"当天发货 保修3年 送影视会员 咨询客服\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"productTypeName\":\"实物商品\",\"auditStatuses\":null,\"auditStatusName\":\"待审核\",\"onlineStatusName\":\"已下架\",\"startCreateTime\":null,\"endCreateTime\":null,\"productExtra\":{\"productId\":1,\"productDetails\":\"商品详情\",\"productSpecifications\":\"商品规格参数\",\"productServices\":\"商品服务\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},\"productSaleSpecs\":[{\"productId\":1,\"specNo\":\"11\",\"specName\":\"4G全网通\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"12\",\"specName\":\"5G全网通\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"21\",\"specName\":\"亮黑色\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"22\",\"specName\":\"釉白色\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"23\",\"specName\":\"秘银色\",\"specIndex\":3,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"24\",\"specName\":\"夏日胡杨\",\"specIndex\":4,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"25\",\"specName\":\"秋日胡杨\",\"specIndex\":5,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"31\",\"specName\":\"8+128GB\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"32\",\"specName\":\"8+256GB\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"}],\"productSaleStocks\":[{\"productId\":1,\"specNo\":\"11:21:31\",\"specName\":\"4G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:21:32\",\"specName\":\"4G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:31\",\"specName\":\"4G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:32\",\"specName\":\"4G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:31\",\"specName\":\"4G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:32\",\"specName\":\"4G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:31\",\"specName\":\"4G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:32\",\"specName\":\"4G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:31\",\"specName\":\"4G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:32\",\"specName\":\"4G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:31\",\"specName\":\"5G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:32\",\"specName\":\"5G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:31\",\"specName\":\"5G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:32\",\"specName\":\"5G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:31\",\"specName\":\"5G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:32\",\"specName\":\"5G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:31\",\"specName\":\"5G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:32\",\"specName\":\"5G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:31\",\"specName\":\"5G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:32\",\"specName\":\"5G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null}]}";
        ProductAggregate source = JsonUtils.json2Object(productJson, ProductAggregate.class);
        QueryProductResponse target = new QueryProductResponse();

        BeanCopier beanCopier = BeanCopier.create(ProductAggregate.class, QueryProductResponse.class, true);
        beanCopier.copy(source, target, new DefaultConverter());
        System.out.println(target);
    }

    protected static void test2() {
        Account source = new Account();
        source.setId(1234567890L);
        source.setName("阿三");
        source.setEnabled(true);
        source.setCreated(LocalDateTime.now());

        AccountDTO target = new AccountDTO();

        BeanCopier beanCopier = BeanCopier.create(Account.class, AccountDTO.class, true);
        beanCopier.copy(source, target, new DefaultConverter());
        System.out.println(source);
        System.out.println(target);
    }

    protected static void test3() {
        AccountDTO source = new AccountDTO();
        source.setId(1234567890L);
        source.setName("阿三");
        source.setEnabled(true);
        source.setCreated(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Account target = new Account();

        BeanCopier beanCopier = BeanCopier.create(AccountDTO.class, Account.class, true);
        beanCopier.copy(source, target, new DefaultConverter());
        System.out.println(source);
        System.out.println(target);
    }

    protected static void test4() {
        String productJson = "{\"productId\":1,\"productName\":\"24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通\",\"productUrl\":\"https://detail.tmall.com/item.htm?id=633658852628\",\"productTags\":\"华为手机 5G mate40pro\",\"productType\":1,\"auditStatus\":1,\"onlineStatus\":1,\"shopId\":111212422,\"remark\":\"当天发货 保修3年 送影视会员 咨询客服\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"productTypeName\":\"实物商品\",\"auditStatuses\":null,\"auditStatusName\":\"待审核\",\"onlineStatusName\":\"已下架\",\"startCreateTime\":null,\"endCreateTime\":null,\"productExtra\":{\"productId\":1,\"productDetails\":\"商品详情\",\"productSpecifications\":\"商品规格参数\",\"productServices\":\"商品服务\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},\"productSaleSpecs\":[{\"productId\":1,\"specNo\":\"11\",\"specName\":\"4G全网通\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"12\",\"specName\":\"5G全网通\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"21\",\"specName\":\"亮黑色\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"22\",\"specName\":\"釉白色\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"23\",\"specName\":\"秘银色\",\"specIndex\":3,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"24\",\"specName\":\"夏日胡杨\",\"specIndex\":4,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"25\",\"specName\":\"秋日胡杨\",\"specIndex\":5,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"31\",\"specName\":\"8+128GB\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"32\",\"specName\":\"8+256GB\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"}],\"productSaleStocks\":[{\"productId\":1,\"specNo\":\"11:21:31\",\"specName\":\"4G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:21:32\",\"specName\":\"4G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:31\",\"specName\":\"4G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:32\",\"specName\":\"4G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:31\",\"specName\":\"4G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:32\",\"specName\":\"4G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:31\",\"specName\":\"4G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:32\",\"specName\":\"4G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:31\",\"specName\":\"4G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:32\",\"specName\":\"4G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:31\",\"specName\":\"5G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:32\",\"specName\":\"5G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:31\",\"specName\":\"5G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:32\",\"specName\":\"5G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:31\",\"specName\":\"5G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:32\",\"specName\":\"5G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:31\",\"specName\":\"5G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:32\",\"specName\":\"5G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:31\",\"specName\":\"5G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:32\",\"specName\":\"5G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null}]}";
        ProductAggregate source = JsonUtils.json2Object(productJson, ProductAggregate.class);
        int total = 100000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < total; i++){
            QueryProductResponse target = new QueryProductResponse();
            BeanCopier beanCopier = BeanCopier.create(ProductAggregate.class, QueryProductResponse.class, true);
            beanCopier.copy(source, target, new DefaultConverter());
        }
        long end = System.currentTimeMillis();
        long delta = (end - start);
        System.out.println("delta = " + delta + ", avg = " + delta / total);
    }

    protected static void test5() {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        String productJson = "{\"productId\":1,\"productName\":\"24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通\",\"productUrl\":\"https://detail.tmall.com/item.htm?id=633658852628\",\"productTags\":\"华为手机 5G mate40pro\",\"productType\":1,\"auditStatus\":1,\"onlineStatus\":1,\"shopId\":111212422,\"remark\":\"当天发货 保修3年 送影视会员 咨询客服\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"productTypeName\":\"实物商品\",\"auditStatuses\":null,\"auditStatusName\":\"待审核\",\"onlineStatusName\":\"已下架\",\"startCreateTime\":null,\"endCreateTime\":null,\"productExtra\":{\"productId\":1,\"productDetails\":\"商品详情\",\"productSpecifications\":\"商品规格参数\",\"productServices\":\"商品服务\",\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},\"productSaleSpecs\":[{\"productId\":1,\"specNo\":\"11\",\"specName\":\"4G全网通\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"12\",\"specName\":\"5G全网通\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"21\",\"specName\":\"亮黑色\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"22\",\"specName\":\"釉白色\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"23\",\"specName\":\"秘银色\",\"specIndex\":3,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"24\",\"specName\":\"夏日胡杨\",\"specIndex\":4,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"25\",\"specName\":\"秋日胡杨\",\"specIndex\":5,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"31\",\"specName\":\"8+128GB\",\"specIndex\":1,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"},{\"productId\":1,\"specNo\":\"32\",\"specName\":\"8+256GB\",\"specIndex\":2,\"remark\":null,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\"}],\"productSaleStocks\":[{\"productId\":1,\"specNo\":\"11:21:31\",\"specName\":\"4G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:21:32\",\"specName\":\"4G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:31\",\"specName\":\"4G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:22:32\",\"specName\":\"4G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:31\",\"specName\":\"4G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:23:32\",\"specName\":\"4G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:31\",\"specName\":\"4G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:24:32\",\"specName\":\"4G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:31\",\"specName\":\"4G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"11:25:32\",\"specName\":\"4G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:31\",\"specName\":\"5G全网通:亮黑色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:21:32\",\"specName\":\"5G全网通:亮黑色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:31\",\"specName\":\"5G全网通:釉白色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:22:32\",\"specName\":\"5G全网通:釉白色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:31\",\"specName\":\"5G全网通:秘银色:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:23:32\",\"specName\":\"5G全网通:秘银色:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:31\",\"specName\":\"5G全网通:夏日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:24:32\",\"specName\":\"5G全网通:夏日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:31\",\"specName\":\"5G全网通:秋日胡杨:8+128GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null},{\"productId\":1,\"specNo\":\"12:25:32\",\"specName\":\"5G全网通:秋日胡杨:8+256GB\",\"sellPrice\":619900,\"stock\":999,\"createTime\":\"2022-01-05 17:38:13\",\"updateTime\":\"2022-01-05 17:38:13\",\"minSellPrice\":null,\"maxSellPrice\":null,\"minStock\":null,\"maxStock\":null}]}";
        ProductAggregate source = JsonUtils.json2Object(productJson, ProductAggregate.class);
        int total = 100000;
        long start = System.currentTimeMillis();
        for(int i = 0; i < total; i++){
            String json = JsonUtils.object2Json(source);
            QueryProductResponse target = JsonUtils.json2Object(json, QueryProductResponse.class);
        }
        long end = System.currentTimeMillis();
        long delta = (end - start);
        System.out.println("delta = " + delta + ", avg = " + delta / total);
    }

    public static class Account {

        private Long id;

        private String name;

        private boolean enabled;

        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private LocalDateTime created;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public LocalDateTime getCreated() {
            return created;
        }

        public void setCreated(LocalDateTime created) {
            this.created = created;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", enabled=" + enabled +
                    ", created=" + created +
                    '}';
        }
    }

    public static class AccountDTO {

        private long id;

        private CharSequence name;

        private Boolean enabled;

        private String created;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public CharSequence getName() {
            return name;
        }

        public void setName(CharSequence name) {
            this.name = name;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        @Override
        public String toString() {
            return "AccountDTO{" +
                    "id=" + id +
                    ", name=" + name +
                    ", enabled=" + enabled +
                    ", created='" + created + '\'' +
                    '}';
        }
    }

}
