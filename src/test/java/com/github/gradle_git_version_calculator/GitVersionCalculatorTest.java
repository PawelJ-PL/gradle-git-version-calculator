package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.git_commands.DescribeCommand;
import org.junit.Before;
import org.junit.Test;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    private final String path = "/tmp/repo/xx";

    @Before
    public void setUp()  {
        service = new GitVersionCalculator();
    }
    
    @Test
    public void test1() {
        //given
        
        //when
        DescribeCommand describeCommand = new DescribeCommand(path);
        describeCommand.setTags(true);
        describeCommand.setAbbrev(0);
        System.out.println(describeCommand.call());
        
        //then
        //System.out.println(result);
    }
}
