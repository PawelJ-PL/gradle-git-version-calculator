package com.github.gradle_git_version_calculator;


import com.github.gradle_git_version_calculator.models.SemanticVersion;

public class GitVersionCalculator {
    
    private final GitRepository gitRepository;
    
    private final String DEFAULT_VERSION = "0.0.1-SNAPSHOT";
    
    public GitVersionCalculator(GitRepository gitRepository) {
        this.gitRepository = gitRepository;
    }

    public SemanticVersion calculateSemVer() {
        return gitRepository.getLatestTag()
                .map(tag -> {
                    SemanticVersion result = SemanticVersion.fromString(tag);
                    result = addDistance(result, tag);
                    result = addCleanInfo(result);
                    return result;
                })
                .orElse(SemanticVersion.fromString(DEFAULT_VERSION));
    }
    
    private SemanticVersion addDistance(SemanticVersion version, String baseTag) {
        Integer distance = gitRepository.getCommitsSinceTag(baseTag);
        if (distance < 1) {
            return version;
        }
        return getVersionWithUpdatedBuildMetadata(version, distance.toString());
    }
    
    private SemanticVersion addCleanInfo(SemanticVersion version) {
        if (gitRepository.isClean()) {
            return version;
        }
        return getVersionWithUpdatedBuildMetadata(version, "dirty");
    }
    
    private SemanticVersion getVersionWithUpdatedBuildMetadata(SemanticVersion originalVersion, String newElement) {
        SemanticVersion result = originalVersion.copy();
        StringBuilder stringBuilder = new StringBuilder();
        if (originalVersion.getBuildMetadata() != null) {
            stringBuilder.append(originalVersion.getBuildMetadata());
            stringBuilder.append(".");
        }
        stringBuilder.append(newElement);
        result.setBuildMetadata(stringBuilder.toString());
        return result;
    }

}
