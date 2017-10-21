package com.github.gradle_git_version_calculator;

import org.junit.Before;
import org.junit.Test;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    //private final String path = "/home/pawel/IdeaProjects/MyHomeInfo/";
    private final String path = "/tmp/repo/xx";

    @Before
    public void setUp()  {
        service = new GitVersionCalculator();
    }
    
    @Test
    public void test1() {
        //given
        
        //when
        String result = service.calculateVersion();
        
        //then
        System.out.println(result);
    }
}