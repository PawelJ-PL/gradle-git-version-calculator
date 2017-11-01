package com.github.gradle_git_version_calculator.git_commands;

public class StatusCommand extends AbstractGitCommand {
    
    public StatusCommand(String gitRepo) {
        super(gitRepo);
    }
    
    public StatusCommand(String gitRepo, String pathToGitExecutable) {
        super(gitRepo, pathToGitExecutable);
    }
    
    @Override
    protected String getCommand() {
        return "status";
    }
    
    public StatusCommand setPorcelain(boolean porcelain) {
        if (porcelain) {
            args.add("--porcelain");
        } else {
            args.remove("--porcelain");
        }
        return this;
    }
}
