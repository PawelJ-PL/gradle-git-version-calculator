package com.github.gradle_git_version_calculator.git_commands;

public class DescribeCommand extends AbstractGitCommand {
    
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
    
    public DescribeCommand setTags(boolean withTags) {
        if (withTags) {
            args.add("--tags");
        } else {
            args.remove("--tags");
        }
        return this;
    }
    
    public DescribeCommand setMatch(String matcher) {
        args.add("--match=" + matcher);
        return this;
    }
    
    public DescribeCommand setAbbrev(int abbrev) {
        args.add("--abbrev=" + Integer.toString(abbrev));
        return this;
    }
}
