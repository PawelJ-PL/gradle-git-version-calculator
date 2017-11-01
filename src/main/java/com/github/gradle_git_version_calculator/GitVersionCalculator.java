package com.github.gradle_git_version_calculator;


import com.github.gradle_git_version_calculator.models.SemanticVersion;

public class GitVersionCalculator {
    
    private final GitRepository gitRepository;
    
    private final String DEFAULT_VERSION = "0.0.1-SNAPSHOT";
    
    public GitVersionCalculator(GitRepository gitRepository) {
        this.gitRepository = gitRepository;
    }
    
    public SemanticVersion calculateSemVer() {
        return calculateSemVer("");
    }

    public SemanticVersion calculateSemVer(String prefix) {
        return gitRepository.getLatestTag(prefix)
                .map(tag -> {
                    SemanticVersion result = SemanticVersion.fromString(extractVersionFromTag(tag, prefix));
                    result = addDistance(result, tag);
                    result = addCleanInfo(result);
                    return result;
                })
                .orElse(SemanticVersion.fromString(DEFAULT_VERSION));
    }
    
    private String extractVersionFromTag(String tag, String prefix) {
        if (!tag.startsWith(prefix)) {
            throw new RuntimeException(String.format("%s is not prefix of %s", prefix, tag));
        }
        return tag.replaceFirst(String.format("^%s", prefix), "");
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
        return getVersionWithUpdatedBuildMetadata(version, "dev");
    }
    
    private SemanticVersion getVersionWithUpdatedBuildMetadata(SemanticVersion originalVersion, String newElement) {
        SemanticVersion result = originalVersion.copy();
        StringBuilder stringBuilder = new StringBuilder();
        result.getBuildMetadata().ifPresent(data -> {
            stringBuilder.append(data);
            stringBuilder.append(".");
        });
        stringBuilder.append(newElement);
        result.setBuildMetadata(stringBuilder.toString());
        return result;
    }

}
