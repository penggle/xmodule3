package com.penglecode.xmodule.samples.product;

import com.google.common.collect.Lists;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.samples.product.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductTypeEnum;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/4 15:44
 */
public class ProductTestHelper {

    public static Object[] genProductTests4Create() {
        String nowTime = DateTimeUtils.formatNow();
        ProductBaseInfo productBase = new ProductBaseInfo();
        productBase.setProductName("24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通");
        productBase.setProductUrl("https://detail.tmall.com/item.htm?id=633658852628");
        productBase.setProductTags("华为手机 5G mate40pro");
        productBase.setProductType(ProductTypeEnum.PHYSICAL_PRODUCT.getTypeCode());
        productBase.setAuditStatus(ProductAuditStatusEnum.WAIT_AUDIT.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.OFFLINE.getStatusCode());
        productBase.setShopId(111212422L);
        productBase.setRemark("当天发货 保修3年 送影视会员 咨询客服");
        productBase.setCreateTime(nowTime);
        productBase.setUpdateTime(nowTime);

        ProductExtraInfo productExtra = new ProductExtraInfo();
        productExtra.setProductId(productBase.getProductId());
        productExtra.setProductDetails("商品详情");
        productExtra.setProductSpecifications("商品规格参数");
        productExtra.setProductServices("商品服务");
        productExtra.setCreateTime(nowTime);
        productExtra.setUpdateTime(nowTime);

        List<ProductSaleSpec> productSaleSpecs = new ArrayList<>();
        List<ProductSaleStock> productSaleStocks = new ArrayList<>();

        List<String> nets = Arrays.asList("11:4G全网通", "12:5G全网通");
        List<String> colors = Arrays.asList("21:亮黑色", "22:釉白色", "23:秘银色", "24:夏日胡杨", "25:秋日胡杨");
        List<String> storages = Arrays.asList("31:8+128GB", "32:8+256GB");
        List<List<String>> specs = Arrays.asList(nets, colors, storages);

        for(int i = 0, len1 = specs.size(); i < len1; i++) {
            for(int j = 0, len2 = specs.get(i).size(); j < len2; j++) {
                String[] specNoAndName = specs.get(i).get(j).split(":");
                ProductSaleSpec productSpec = new ProductSaleSpec();
                productSpec.setProductId(productBase.getProductId());
                productSpec.setSpecNo(specNoAndName[0]);
                productSpec.setSpecName(specNoAndName[1]);
                productSpec.setSpecIndex(Integer.valueOf("" + productSpec.getSpecNo().charAt(1)));
                productSpec.setCreateTime(nowTime);
                productSpec.setUpdateTime(nowTime);
                productSaleSpecs.add(productSpec);
            }
        }

        Map<Character,List<ProductSaleSpec>> groupedProductSaleSpecs = productSaleSpecs.stream().collect(Collectors.groupingBy(spec -> spec.getSpecNo().charAt(0)));
        List<ProductSaleSpec> productSaleSpecs0 = groupedProductSaleSpecs.get('1');
        List<ProductSaleSpec> productSaleSpecs1 = groupedProductSaleSpecs.get('2');
        List<ProductSaleSpec> productSaleSpecs2 = groupedProductSaleSpecs.get('3');
        List<List<ProductSaleSpec>> cartesians = Lists.cartesianProduct(productSaleSpecs0, productSaleSpecs1, productSaleSpecs2); //笛卡尔积

        for (List<ProductSaleSpec> cartesian : cartesians) {
            StringBuilder specNos = new StringBuilder();
            StringBuilder specNames = new StringBuilder();
            for (int j = 0, len2 = cartesian.size(); j < len2; j++) {
                specNos.append(cartesian.get(j).getSpecNo());
                specNames.append(cartesian.get(j).getSpecName());
                if (j != len2 - 1) {
                    specNos.append(":");
                    specNames.append(":");
                }
            }
            ProductSaleStock productSaleStock = new ProductSaleStock();
            productSaleStock.setProductId(productBase.getProductId());
            productSaleStock.setSpecNo(specNos.toString());
            productSaleStock.setSpecName(specNames.toString());
            productSaleStock.setSellPrice(619900L);
            productSaleStock.setStock(999);
            productSaleStock.setCreateTime(nowTime);
            productSaleStock.setUpdateTime(nowTime);
            productSaleStocks.add(productSaleStock);
        }
        return new Object[] {productBase, productExtra, productSaleSpecs, productSaleStocks};
    }

    public static Object[] genProductTests4Modify(Long productId) {
        String nowTime = DateTimeUtils.formatNow();
        ProductBaseInfo productBase = new ProductBaseInfo();
        productBase.setProductId(productId);
        productBase.setProductName("24期免息【当天发】Huawei/华为Mate40 5G手机官方旗舰店50pro直降mate40e官网30正品4G鸿蒙正品30全网通AAA");
        productBase.setProductUrl("https://detail.tmall.com/item.htm?id=633658852628");
        productBase.setProductTags("华为手机 5G mate40proAAA");
        productBase.setProductType(ProductTypeEnum.PHYSICAL_PRODUCT.getTypeCode());
        productBase.setAuditStatus(ProductAuditStatusEnum.WAIT_AUDIT.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.OFFLINE.getStatusCode());
        productBase.setShopId(111212422L);
        productBase.setRemark("当天发货 保修3年 送影视会员 咨询客服AAA");
        productBase.setCreateTime(nowTime);
        productBase.setUpdateTime(nowTime);

        ProductExtraInfo productExtra = new ProductExtraInfo();
        productExtra.setProductId(productBase.getProductId());
        productExtra.setProductDetails("商品详情AAA");
        productExtra.setProductSpecifications("商品规格参数AAA");
        productExtra.setProductServices("商品服务AAA");
        productExtra.setCreateTime(nowTime);
        productExtra.setUpdateTime(nowTime);

        List<ProductSaleSpec> productSaleSpecs = new ArrayList<>();
        List<ProductSaleStock> productSaleStocks = new ArrayList<>();

        List<String> nets = Arrays.asList("11:4G全网通", "12:5G全网通");
        List<String> colors = Arrays.asList("22:釉白色", "23:秘银色", "24:夏日胡杨", "25:秋日胡杨", "26:暗黑色");
        List<String> storages = Arrays.asList("31:8+128GB", "32:8+256GB");
        List<List<String>> specs = Arrays.asList(nets, colors, storages);

        for(int i = 0, len1 = specs.size(); i < len1; i++) {
            for(int j = 0, len2 = specs.get(i).size(); j < len2; j++) {
                String[] specNoAndName = specs.get(i).get(j).split(":");
                ProductSaleSpec productSpec = new ProductSaleSpec();
                productSpec.setProductId(productBase.getProductId());
                productSpec.setSpecNo(specNoAndName[0]);
                productSpec.setSpecName(specNoAndName[1]);
                productSpec.setSpecIndex(Integer.valueOf("" + productSpec.getSpecNo().charAt(1)));
                productSpec.setCreateTime(nowTime);
                productSpec.setUpdateTime(nowTime);
                productSaleSpecs.add(productSpec);
            }
        }

        Map<Character,List<ProductSaleSpec>> groupedProductSaleSpecs = productSaleSpecs.stream().collect(Collectors.groupingBy(spec -> spec.getSpecNo().charAt(0)));
        List<ProductSaleSpec> productSaleSpecs0 = groupedProductSaleSpecs.get('1');
        List<ProductSaleSpec> productSaleSpecs1 = groupedProductSaleSpecs.get('2');
        List<ProductSaleSpec> productSaleSpecs2 = groupedProductSaleSpecs.get('3');
        List<List<ProductSaleSpec>> cartesians = Lists.cartesianProduct(productSaleSpecs0, productSaleSpecs1, productSaleSpecs2); //笛卡尔积

        for (List<ProductSaleSpec> cartesian : cartesians) {
            StringBuilder specNos = new StringBuilder();
            StringBuilder specNames = new StringBuilder();
            for (int j = 0, len2 = cartesian.size(); j < len2; j++) {
                specNos.append(cartesian.get(j).getSpecNo());
                specNames.append(cartesian.get(j).getSpecName());
                if (j != len2 - 1) {
                    specNos.append(":");
                    specNames.append(":");
                }
            }
            ProductSaleStock productSaleStock = new ProductSaleStock();
            productSaleStock.setProductId(productBase.getProductId());
            productSaleStock.setSpecNo(specNos.toString());
            productSaleStock.setSpecName(specNames.toString());
            productSaleStock.setSellPrice(619900L);
            productSaleStock.setStock(999);
            productSaleStock.setCreateTime(nowTime);
            productSaleStock.setUpdateTime(nowTime);
            productSaleStocks.add(productSaleStock);
        }
        return new Object[] {productBase, productExtra, productSaleSpecs, productSaleStocks};
    }

}
