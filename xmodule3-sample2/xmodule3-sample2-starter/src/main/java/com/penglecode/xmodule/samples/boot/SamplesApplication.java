package com.penglecode.xmodule.samples.boot;

import com.penglecode.xmodule.BasePackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.dal.EnableDalAutoConfigure;
import org.springframework.boot.autoconfigure.dal.NamedDatabase;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/2/27 16:54
 */
@EnableDalAutoConfigure({@NamedDatabase("default"), @NamedDatabase("product")})
@SpringBootApplication(scanBasePackageClasses= BasePackage.class)
public class SamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamplesApplication.class, args);
    }

}
