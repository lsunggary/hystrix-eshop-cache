package com.scott.eshop.cache;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.scott.eshop.cache.filter.HystrixRequestContextFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @ClassName Application
 * @Description
 * @Author 47980
 * @Date 2020/5/7 20:35
 * @Version V_1.0
 **/
@SpringBootApplication
@MapperScan("com.scott.eshop.cache.mapper")
public class Application {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public FilterRegistrationBean hystrixRegistrationBean () {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
                new HystrixRequestContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new HystrixMetricsStreamServlet());
        registration.addUrlMappings("/hystrix.stream");
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
