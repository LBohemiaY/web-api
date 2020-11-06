package org.bohemia.webapi.config.dynamicds;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态数据源切换处理器
 * 这里AOP
 * @author hitty
 *
 */
@Aspect
@Order(-1)  // 该切面应当先于 @Transactional 执行
@Component
public class DynamicDataSourceAspect {

    /**
     * 切换数据源
     * @param point
     * @param switchDataSource
     */
    @Before("@annotation(switchDataSource))")
    public void switchDataSource(JoinPoint point, SwitchDataSource switchDataSource) {
        if (!DynamicDataSourceContextHolder.containDataSourceKey(switchDataSource.value())) {
            System.out.println("SwitchDataSource [{}] doesn't exist, use default SwitchDataSource [{}] " + switchDataSource.value());
        } else {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(switchDataSource.value());
            System.out.println("Switch SwitchDataSource to [" + DynamicDataSourceContextHolder.getDataSourceKey()
                    + "] in Method [" + point.getSignature() + "]");
        }
    }

    /**
     * 重置数据源
     * @param point
     * @param switchDataSource
     */
    @After("@annotation(switchDataSource))")
    public void restoreDataSource(JoinPoint point, SwitchDataSource switchDataSource) {
        // 将数据源置为默认数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
        System.out.println("Restore SwitchDataSource to [" + DynamicDataSourceContextHolder.getDataSourceKey()
                + "] in Method [" + point.getSignature() + "]");
    }
}