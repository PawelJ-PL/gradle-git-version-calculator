package com.github.gradle_git_version_calculator.git_commands;

import com.github.gradle_git_version_calculator.GitCommandResult;

public interface GitCommand {
    
    GitCommandResult call();
    
}
