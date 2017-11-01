package com.github.gradle_git_version_calculator.Exceptions;

import com.github.gradle_git_version_calculator.models.GitCommandResult;

public class GitCommandError extends RuntimeException {
    
    private final GitCommandResult commandResult;
    
    public GitCommandError(GitCommandResult commandResult) {
        super(commandResult.toString());
        this.commandResult = commandResult;
    }
    
    public GitCommandError(GitCommandResult commandResult, Throwable cause) {
        super(commandResult.toString(), cause);
        this.commandResult = commandResult;
    }
}
