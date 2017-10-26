package com.github.gradle_git_version_calculator;


import com.github.gradle_git_version_calculator.models.SemanticVersion;

public class GitVersionCalculator {
    
    private final GitRepository gitRepository;
    
    private final String DEFAULT_VERSION = "0.0.1-SNAPSHOT";
    
    public GitVersionCalculator(GitRepository gitRepository) {
        this.gitRepository = gitRepository;
    }

    public SemanticVersion calculateSemVer() {
        String tag = gitRepository.getLatestTag().orElse(DEFAULT_VERSION);
        //System.out.println(tag);
        //System.out.println(gitRepository.getCommitsSinceTag("0.1.0"));
        return new SemanticVersion(1,1,1);
    }

}
