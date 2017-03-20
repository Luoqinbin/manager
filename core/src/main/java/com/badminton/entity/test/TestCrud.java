package com.badminton.entity.test;

import com.badminton.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 测试crud
 */
@Table(name = "test_crud")
public class TestCrud extends BaseEntity{

    @Column(name = "test_name_1")
    private String testName1;
    @Column(name = "test_name_2")
    private String testName2;
    @Column(name = "test_name_3")
    private String testName3;

    public String getTestName1() {
        return testName1;
    }

    public void setTestName1(String testName1) {
        this.testName1 = testName1;
    }

    public String getTestName2() {
        return testName2;
    }

    public void setTestName2(String testName2) {
        this.testName2 = testName2;
    }

    public String getTestName3() {
        return testName3;
    }

    public void setTestName3(String testName3) {
        this.testName3 = testName3;
    }
}
