package com.github.gradle_git_version_calculator;

import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    private final String path = "/home/pawel/IdeaProjects/MyHomeInfo/";
    //private final String path = "/tmp/repo";
    
    @Before
    public void setUp() throws IOException {
        Git git = Git.open(new File(path));
        service = new GitVersionCalculator(git);
    }
    
    @Test
    public void test1() {
        //given
        
        //when
        service.calculateSemVer();
        
        //then
    }
}
