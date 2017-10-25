package com.github.gradle_git_version_calculator.git_commands;

public class RevListCommand extends AbstractGitCommand {
    
    public RevListCommand(String gitRepo) {
        super(gitRepo);
    }
    
    public RevListCommand(String gitRepo, String pathToGitExecutable) {
        super(gitRepo, pathToGitExecutable);
    }
    
    @Override
    protected String getCommand() {
        return "rev-list";
    }
    
    public RevListCommand setSpec(String spec) {
        args.add(spec);
        return this;
    }
    
    public RevListCommand setCount(boolean count) {
        if (count) {
            args.add("--count");
        } else {
            args.remove("--count");
        }
        return this;
    }
}
