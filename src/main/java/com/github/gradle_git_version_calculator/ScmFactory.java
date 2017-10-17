package com.github.gradle_git_version_calculator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class ScmFactory {

    private Git git;

    public ScmFactory(String path) {
        git = new Git(getRepo(path));
    }

    public Git getScm() {
        return git;
    }

    private Repository getRepo(String path) {
        RepositoryBuilder repoBuilder = new RepositoryBuilder();
        repoBuilder.setMustExist(true);
        repoBuilder.findGitDir(new File(path));
        try {
            return repoBuilder.build();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

}
