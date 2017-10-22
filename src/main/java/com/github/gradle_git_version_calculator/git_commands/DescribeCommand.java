package com.github.gradle_git_version_calculator.git_commands;

import com.github.gradle_git_version_calculator.GitCommandResult;

import java.util.ArrayList;
import java.util.List;

public class DescribeCommand extends AbstractGitCommand {
    
    private List<String> args = new ArrayList<>();
    
    public DescribeCommand() {
        super();
    }
    
    public DescribeCommand(String gitRepo) {
        super(gitRepo);
    }
    
    public DescribeCommand(String gitRepo, String pathToGitExecutable) {
        super(gitRepo, pathToGitExecutable);
    }
    
    @Override
    protected String getCommand() {
        return "describe";
    }
    
    public String call() {
        GitCommandResult result = process(args);
        if (result.getStatusCode() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(result.getStdOutAsString());
            stringBuilder.append("\n\n");
            stringBuilder.append(result.getStdErrAsString());
            throw new RuntimeException(stringBuilder.toString());
        }
        System.out.println(result.getStdErrAsString());
        return result.getStdErrAsString();
    }
    
    public DescribeCommand setTags(boolean withTags) {
        if (withTags) {
            args.add("--tags");
        } else {
            args.remove("--tags");
        }
        return this;
    }
    
    public DescribeCommand setAbbrev(int abbrev) {
        args.add("--abbrev=" + Integer.toString(abbrev));
        return this;
    }
}
