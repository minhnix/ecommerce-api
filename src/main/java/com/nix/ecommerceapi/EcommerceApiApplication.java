package com.nix.ecommerceapi;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        EcommerceApiApplication.class,
        Jsr310JpaConverters.class
})
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class EcommerceApiApplication {
    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
    }

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApiApplication.class, args);
    }

}
