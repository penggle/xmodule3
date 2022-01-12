package com.penglecode.xmodule.common.support;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

/**
 * 默认的全局ConversionService
 *
 * @author pengpeng
 * @version 1.0
 * @since 2021/4/12 10:41
 */
public final class DefaultConversionService {

    private static volatile FormattingConversionService conversionService;

    private DefaultConversionService() {}

    public static FormattingConversionService getConversionService() {
        if(conversionService == null) {
            synchronized (DefaultConversionService.class) {
                if(conversionService == null) {
                    conversionService = createDefaultConversionService();
                }
            }
        }
        return conversionService;
    }

    private static FormattingConversionService createDefaultConversionService() {
        FormattingConversionService conversionService = (FormattingConversionService) ApplicationConversionService.getSharedInstance();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters(conversionService);
        return conversionService;
    }

}
