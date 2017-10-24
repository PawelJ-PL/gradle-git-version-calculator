package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.Exceptions.NoNamesFoundError;
import com.github.gradle_git_version_calculator.git_commands.GitCommand;

import java.util.Optional;

public class GitRepository {
    
    private final GitCommandsFactory commandsFactory;
    
    public GitRepository(GitCommandsFactory commandsFactory) {
        this.commandsFactory = commandsFactory;
    }
    
    public Optional<String> getLatestTag() {
        GitCommand command = commandsFactory.getDescribe().setTags(true).setAbbrev(0);
        GitCommandResult result;
        try {
            result = command.call();
        } catch (NoNamesFoundError err) {
            //TODO add logger here
            return Optional.empty();
        }
        if (result.getStdOut().size() != 1) {
            throw new RuntimeException(String.format("Expected 1 line, got %d:\n%s",
                                                     result.getStdOut().size(),
                                                     result.getStdOutAsString()));
        }
        return Optional.of(result.getStdOutAsString().trim());
    }
    
}
