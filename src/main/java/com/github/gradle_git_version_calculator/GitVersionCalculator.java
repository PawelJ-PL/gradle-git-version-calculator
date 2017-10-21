package com.github.gradle_git_version_calculator;

import org.eclipse.jgit.api.DescribeCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class GitVersionCalculator {
    
    private final Git git;
    
    public GitVersionCalculator(ScmFactory scmFactory) {
        git = scmFactory.getScm();
    }
    
    public String calculateVersion() {
        return getVersionFromTag();
    }

    private String getVersionFromTag() {
        try {
            DescribeCommand describeCommand = git.describe();
            return describeCommand.call();
        } catch (GitAPIException err) {
            throw new RuntimeException(err);
        }
    }

}
