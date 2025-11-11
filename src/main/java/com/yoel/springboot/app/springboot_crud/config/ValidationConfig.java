package com.yoel.springboot.app.springboot_crud.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import jakarta.validation.Validator;

@Configuration
public class ValidationConfig {

  @Bean
    public Validator validator(AutowireCapableBeanFactory beanFactory) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        validatorFactoryBean.setConstraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory));
        return validatorFactoryBean;
    }


}
