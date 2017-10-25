package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.git_commands.DescribeCommand;
import com.github.gradle_git_version_calculator.git_commands.RevListCommand;
import com.github.gradle_git_version_calculator.git_commands.StatusCommand;

public class GitCommandsFactory {
    
    private final String path;
    
    public GitCommandsFactory(String path) {
        this.path = path;
    }
    
    public DescribeCommand getDescribe() {
        return new DescribeCommand(path);
    }
    
    public StatusCommand getStatus() {
        return new StatusCommand(path);
    }
    
    public RevListCommand getRevList() {
        return new RevListCommand(path);
    }
    
}
