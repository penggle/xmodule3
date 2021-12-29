CREATE TABLE t_product_info (
    product_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    product_name VARCHAR(191) NOT NULL COMMENT '商品名称',
    product_url VARCHAR(255) NOT NULL COMMENT '商品URL',
    product_tags VARCHAR(255) NULL DEFAULT NULL COMMENT '商品标签',
    product_type TINYINT(1) NOT NULL DEFAULT '1' COMMENT '商品类型：0-虚拟商品,1-实物商品',
    audit_status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '商品审核状态：0-待审核,1-审核通过,2-审核不通过',
    online_status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '上下架状态：0-已下架,1-已上架',
    shop_id BIGINT UNSIGNED NOT NULL COMMENT '所属店铺ID',
    remark VARCHAR(255) NULL DEFAULT NULL COMMENT '商品备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '最近修改时间',
    PRIMARY KEY (product_id)
) COMMENT='商品信息表' ENGINE=InnoDB;

CREATE TABLE t_product_spec (
    product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    spec_no VARCHAR(2) NOT NULL COMMENT '商品规格编号,两位数字组成',
    spec_name VARCHAR(191) NOT NULL COMMENT '商品规格名称',
    spec_index INT NOT NULL DEFAULT '1' COMMENT '商品规格顺序',
    remark VARCHAR(255) NULL DEFAULT NULL COMMENT '商品规格备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '最近修改时间',
    PRIMARY KEY (product_id, spec_no)
) COMMENT='商品规格型号表' ENGINE=InnoDB;

CREATE TABLE t_product_stock (
    product_id BIGINT(20) UNSIGNED NOT NULL COMMENT '商品ID',
    spec_no VARCHAR(64) NOT NULL COMMENT '商品规格编号,多个t_product_spec.spec_no按顺序拼凑',
    spec_name VARCHAR(64) NOT NULL COMMENT '商品规格编号,多个t_product_spec.spec_name按顺序拼凑',
    sell_price BIGINT(19) NOT NULL COMMENT '商品售价(单位分)',
    stock INT NOT NULL DEFAULT '0' COMMENT '库存量',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '最近修改时间',
    PRIMARY KEY (product_id, spec_no)
) COMMENT='商品规格型号表' ENGINE=InnoDB;