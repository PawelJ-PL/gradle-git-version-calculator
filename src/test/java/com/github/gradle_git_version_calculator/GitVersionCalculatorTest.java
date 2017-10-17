package com.github.gradle_git_version_calculator;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    //private final String path = "/home/pawel/IdeaProjects/MyHomeInfo/";
    private final String path = "/tmp/repo/xx";
    
    @Before
    public void setUp() throws IOException {
        ScmFactory scmFactory = new ScmFactory(path);
        service = new GitVersionCalculator(scmFactory);
    }
    
    @Test
    public void test1() {
        //given
        
        //when
        String result = service.calculateSemVer();
        
        //then
        System.out.println(result);
    }
}
