package com.penglecode.xmodule.samples.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.samples.product.domain.enums.ProductAuditStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductOnlineStatusEnum;
import com.penglecode.xmodule.samples.product.domain.enums.ProductTypeEnum;
import com.penglecode.xmodule.samples.product.domain.model.ProductBaseInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductExtraInfo;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleSpec;
import com.penglecode.xmodule.samples.product.domain.model.ProductSaleStock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2022/1/4 15:44
 */
public class ProductTestHelper {

    private static final Random RANDOM = new Random();

    /**
     * 生成单个测试数据
     */
    public static TestProduct genTestProduct4Create() {
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
        return new TestProduct(productBase, productExtra, productSaleSpecs, productSaleStocks);
    }

    /**
     * 生成单个测试数据
     */
    public static TestProduct genTestProduct4Modify(Long productId) {
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
        return new TestProduct(productBase, productExtra, productSaleSpecs, productSaleStocks);
    }

    public static TestProduct getTestProductFromJD(Long productId, Long sellPrice) throws Exception {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        String jdDetailUrl = "https://item.jd.com/" + productId + ".html";
        JsonNode productJsonNode = getProductJsonNodeFromJd(objectMapper, jdDetailUrl);
        System.out.println(productJsonNode);

        String nowTime = DateTimeUtils.formatNow();
        ProductBaseInfo productBase = new ProductBaseInfo();
        productBase.setProductId(productId);
        productBase.setProductName(productJsonNode.get("name").asText());
        productBase.setProductUrl(jdDetailUrl);
        productBase.setProductTags(productJsonNode.get("catName").toString());
        productBase.setProductType(ProductTypeEnum.PHYSICAL_PRODUCT.getTypeCode());
        productBase.setAuditStatus(ProductAuditStatusEnum.WAIT_AUDIT.getStatusCode());
        productBase.setOnlineStatus(ProductOnlineStatusEnum.OFFLINE.getStatusCode());
        productBase.setShopId(productJsonNode.get("shopId").asLong());
        productBase.setRemark(null);
        productBase.setCreateTime(nowTime);
        productBase.setUpdateTime(nowTime);

        ProductExtraInfo productExtra = new ProductExtraInfo();
        productExtra.setProductId(productId);
        productExtra.setProductDetails(productJsonNode.get("desc").asText());
        productExtra.setProductSpecifications("商品规格" + productId);
        productExtra.setProductServices("商品服务" + productId);
        productExtra.setCreateTime(nowTime);
        productExtra.setUpdateTime(nowTime);

        List<ProductSaleSpec> allProductSaleSpecs = new ArrayList<>();
        List<ProductSaleStock> allProductSaleStocks = new ArrayList<>();

        List<Map<String,Object>> jdProductSaleSpecs = productJsonNode.get("colorSize").traverse(objectMapper).readValueAs(new TypeReference<List<Map<String,Object>>>(){});
        Map<String,Object> firstProductSaleSpec = new LinkedHashMap<>(jdProductSaleSpecs.get(0));
        firstProductSaleSpec.remove("skuId");

        List<List<ProductSaleSpec>> groupedProductSaleSpecs = new ArrayList<>();
        Set<String> productSaleSpecNames = new LinkedHashSet<>();
        int index1 = 1;
        for(Map.Entry<String,Object> entry : firstProductSaleSpec.entrySet()) {
            for(Map<String,Object> jdProductSaleSpec : jdProductSaleSpecs) {
                productSaleSpecNames.add(jdProductSaleSpec.get(entry.getKey()).toString());
            }
            int index2 = 1;
            List<ProductSaleSpec> productSaleSpecs = new ArrayList<>();
            for(String productSaleSpecName : productSaleSpecNames) {
                ProductSaleSpec productSpec = new ProductSaleSpec();
                productSpec.setProductId(productBase.getProductId());
                productSpec.setSpecNo(index1 + "" + index2);
                productSpec.setSpecName(productSaleSpecName);
                productSpec.setSpecIndex(index2);
                productSpec.setCreateTime(nowTime);
                productSpec.setUpdateTime(nowTime);
                productSaleSpecs.add(productSpec);
                allProductSaleSpecs.add(productSpec);
                index2++;
            }
            groupedProductSaleSpecs.add(productSaleSpecs);
            productSaleSpecNames.clear();
            index1++;
        }

        List<List<ProductSaleSpec>> cartesians = Lists.cartesianProduct(groupedProductSaleSpecs); //笛卡尔积
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
            productSaleStock.setSellPrice(sellPrice);
            productSaleStock.setStock(RANDOM.nextInt(1000));
            productSaleStock.setCreateTime(nowTime);
            productSaleStock.setUpdateTime(nowTime);
            allProductSaleStocks.add(productSaleStock);
        }
        return new TestProduct(productBase, productExtra, allProductSaleSpecs, allProductSaleStocks);
    }

    private static JsonNode getProductJsonNodeFromJd(ObjectMapper objectMapper, String jdDetailUrl) throws Exception {
        Document document = Jsoup.connect(jdDetailUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .referrer("https://list.jd.com/")
                .get();
        String targetScript = document.getElementsByTag("script").eq(0).toString();
        //System.out.println(targetScript);
        String head = "var pageConfig = ";
        String tail = "shangjiazizhi:";
        String pageConfigJson = targetScript.substring(targetScript.indexOf(head) + head.length(), targetScript.indexOf(tail)) + tail + "false}}";
        pageConfigJson = pageConfigJson.replace("/**/", "");

        JsonNode pageConfigNode = JsonUtils.createRootJsonNode(objectMapper, pageConfigJson);
        return pageConfigNode.get("product");
    }

    public static void main(String[] args) throws Exception {
        TestProduct testProduct = getTestProductFromJD(100018640796L, 229900L);
        System.out.println(JsonUtils.object2Json(testProduct));
    }

}
