package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.models.SemanticVersion;
import org.junit.Before;
import org.junit.Test;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    private final String path = "/tmp/repo/xx";

    @Before
    public void setUp()  {
        service = new GitVersionCalculator(new GitRepository(new GitCommandsFactory(path)));
    }
    
    @Test
    public void test1() {
        //given
        
        //when
        service.calculateSemVer();
        //SemanticVersion semanticVersion = SemanticVersion.fromString("12.34.56-1011aa.10sd-as.dsasd+fds.0ewfe.fdsf.fdsf");
        SemanticVersion semanticVersion = new SemanticVersion(10, 20, 30, "1000", "0abc.das.0sfdf.sdf");
        System.out.println(semanticVersion);
        
        //then
        //System.out.println(result);
    }
}
