package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.Exceptions.NoNamesFoundError;
import com.github.gradle_git_version_calculator.Exceptions.NotGitRepositoryError;
import com.github.gradle_git_version_calculator.git_commands.GitCommand;
import com.github.gradle_git_version_calculator.models.GitCommandResult;

import java.util.Optional;

public class GitRepository {
    
    private final GitCommandsFactory commandsFactory;
    
    public GitRepository(GitCommandsFactory commandsFactory) {
        this.commandsFactory = commandsFactory;
    }
    
    public Optional<String> getLatestTag() {
        return getLatestTag("");
    }
    
    public Optional<String> getLatestTag(String prefix) {
        GitCommand command =
                commandsFactory.getDescribe().setTags(true).setAbbrev(0).setMatch(String.format("%s*", prefix));
        GitCommandResult result;
        try {
            result = command.call();
        } catch (NoNamesFoundError | NotGitRepositoryError err) {
            //TODO add logger here
            return Optional.empty();
        }
        verifySingleOutputLine(result);
        return Optional.of(result.getStdOutAsString().trim());
    }
    
    public Integer getCommitsSinceTag(String tag) {
        GitCommand command = commandsFactory.getRevList().setSpec(String.format("%s..", tag)).setCount(true);
        GitCommandResult result = command.call();
        verifySingleOutputLine(result);
        return Integer.parseInt(result.getStdOutAsString().trim());
    }
    
    public boolean isClean() {
        GitCommand command = commandsFactory.getStatus().setPorcelain(true);
        GitCommandResult result = command.call();
        return (result.getStdOut().size() == 0);
    }
    
    private void verifySingleOutputLine(GitCommandResult result) {
        if (result.getStdOut().size() != 1) {
            throw new RuntimeException(String.format("Expected 1 line, got %d:\n%s",
                                                     result.getStdOut().size(),
                                                     result.getStdOutAsString()));
        }
    }
    
}
