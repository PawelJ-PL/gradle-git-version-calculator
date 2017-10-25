package com.github.gradle_git_version_calculator.Exceptions;

import com.github.gradle_git_version_calculator.models.GitCommandResult;

public class NotGitRepositoryError extends GitCommandError {
    
    public NotGitRepositoryError(GitCommandResult commandResult) {
        super(commandResult);
    }
    
    public NotGitRepositoryError(GitCommandResult commandResult, Throwable cause) {
        super(commandResult, cause);
    }
}
