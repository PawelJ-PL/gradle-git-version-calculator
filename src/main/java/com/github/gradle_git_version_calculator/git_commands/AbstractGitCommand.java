package com.github.gradle_git_version_calculator.git_commands;

import com.github.gradle_git_version_calculator.GitCommandResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGitCommand {
    
    private String gitExecutable;
    
    private String repoDir;
    
    private final String gitCommand;
    
    public AbstractGitCommand() {
        this(Paths.get(".").toAbsolutePath().normalize().toString());
    }
    
    public AbstractGitCommand(String gitRepo) {
        this(gitRepo, "git");
    }

    public AbstractGitCommand(String gitRepo, String pathToGitExecutable) {
        gitExecutable = pathToGitExecutable;
        repoDir = gitRepo;
        gitCommand = getCommand();
    }
    
    protected abstract String getCommand();
    
    protected GitCommandResult process(List<String> args) {
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
        
        return new GitCommandResult(process.exitValue(), stdout.lines(), stderr.lines());
    }

}
