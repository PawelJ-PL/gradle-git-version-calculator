package com.github.gradle_git_version_calculator;

import org.eclipse.jgit.api.DescribeCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitVersionCalculator {
    
    private final Git git;
    
    public GitVersionCalculator(ScmFactory scmFactory) {
        git = scmFactory.getScm();
    }
    
    public String calculateSemVer() {
        String describeResult;
        try {
            DescribeCommand describeCommand = git.describe();
            describeResult = describeCommand.call();
        } catch (GitAPIException err) {
            throw new RuntimeException(err);
        }
        return describeResult;
    }

    private void validateSemVer(String version) {

    }
}
