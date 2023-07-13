package com.smile.springlearn.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;

}
