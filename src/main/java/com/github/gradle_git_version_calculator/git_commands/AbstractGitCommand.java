package com.github.gradle_git_version_calculator.git_commands;

import com.github.gradle_git_version_calculator.Exceptions.GitCommandError;
import com.github.gradle_git_version_calculator.Exceptions.NoNamesFoundError;
import com.github.gradle_git_version_calculator.models.GitCommandResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGitCommand implements GitCommand{
    
    private String gitExecutable;
    
    private String repoDir;
    
    private final String gitCommand;
    
    protected List<String> args = new ArrayList<>();
    
    public AbstractGitCommand(String gitRepo) {
        this(gitRepo, "git");
    }

    public AbstractGitCommand(String gitRepo, String pathToGitExecutable) {
        gitExecutable = pathToGitExecutable;
        repoDir = gitRepo;
        gitCommand = getCommand();
    }
    
    public GitCommandResult call() {
        GitCommandResult result = process();
        validateResult(result);
        return result;
    }
    
    protected abstract String getCommand();
    
    private GitCommandResult process() {
        List<String > cmd = new ArrayList<>();
        cmd.add(gitExecutable);
        cmd.add(gitCommand);
        cmd.addAll(args);
        ProcessBuilder pBuilder = new ProcessBuilder(cmd);
        pBuilder.directory(new File(repoDir));
        Process process;
        try {
            process = pBuilder.start();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
        try {
            process.waitFor();
        } catch (InterruptedException err) {
            throw new RuntimeException(err);
        }
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        
        return new GitCommandResult(process.exitValue(),
                                    stdout.lines().collect(Collectors.toList()),
                                    stderr.lines().collect(Collectors.toList()));
    }
    
    private void validateResult(GitCommandResult result) {
        if (result.getStatusCode() != 0) {
            if (result.getStdErrAsString().trim().equals("fatal: No names found, cannot describe anything.")) {
                throw new NoNamesFoundError(result);
            } else {
                throw new GitCommandError(result);
            }
        }
    }
}
