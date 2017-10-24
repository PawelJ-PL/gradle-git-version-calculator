package com.github.gradle_git_version_calculator.Exceptions;

import com.github.gradle_git_version_calculator.GitCommandResult;

public class NoNamesFoundError extends GitCommandError {
    
    public NoNamesFoundError(GitCommandResult commandResult) {
        super(commandResult);
    }
    
    public NoNamesFoundError(GitCommandResult commandResult, Throwable cause) {
        super(commandResult, cause);
    }
}
