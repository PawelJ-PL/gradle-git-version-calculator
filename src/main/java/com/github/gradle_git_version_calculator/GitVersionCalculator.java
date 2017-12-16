package com.github.gradle_git_version_calculator;


import com.github.gradle_git_version_calculator.models.SemanticVersion;

public class GitVersionCalculator {
    
    private final GitRepository gitRepository;
    
    private final String DEFAULT_VERSION = "0.0.1-SNAPSHOT";
    
    public GitVersionCalculator(GitRepository gitRepository) {
        this.gitRepository = gitRepository;
    }
    
    public SemanticVersion calculateSemVer() {
        return calculateSemVer("", false);
    }
    
    public SemanticVersion calculateSemVer(String prefix) {
        return calculateSemVer(prefix, false);
    }
    
    public SemanticVersion calculateSemVer(boolean withSnapshot) {
        return calculateSemVer("", withSnapshot);
    }

    public SemanticVersion calculateSemVer(String prefix, boolean withSnapshot) {
        return gitRepository.getLatestTag(prefix)
                .map(tag -> {
                    SemanticVersion result = SemanticVersion.fromString(extractVersionFromTag(tag, prefix));
                    return withSnapshot ?
                            getVersionWithSnapshot(result, tag) :
                            getVersionWithGitMetadata(result, tag);
                            
                })
                .orElse(SemanticVersion.fromString(DEFAULT_VERSION));
    }
    
    private String extractVersionFromTag(String tag, String prefix) {
        if (!tag.startsWith(prefix)) {
            throw new RuntimeException(String.format("%s is not prefix of %s", prefix, tag));
        }
        return tag.replaceFirst(String.format("^%s", prefix), "");
    }
    
    private SemanticVersion getVersionWithGitMetadata(SemanticVersion version, String baseTag) {
        SemanticVersion result = version.copy();
        result = addDistance(result, baseTag);
        result = addCleanInfo(result);
        return result;
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
        String newBuildMetadata = appendVersionPart(result.getBuildMetadata().orElse(""), newElement, ".");
        result.setBuildMetadata(newBuildMetadata);
        return result;
    }
    
    private SemanticVersion getVersionWithSnapshot(SemanticVersion originalVersion, String baseTag) {
        if (!determineSnapshot(baseTag)) {
            return originalVersion;
        }
        String currentPreRelease = originalVersion.getPreRelease().orElse("");
        if (currentPreRelease.endsWith("SNAPSHOT")) {
            return originalVersion;
        }
        SemanticVersion result = originalVersion.copy();
        String newPreRelease = appendVersionPart(currentPreRelease, "SNAPSHOT", "-");
        result.setPreRelease(newPreRelease);
        return result;
    }
    
    private boolean determineSnapshot(String baseTag) {
        return !gitRepository.isClean() || gitRepository.getCommitsSinceTag(baseTag) > 0;
    }
    
    private String appendVersionPart(String original, String newPart, String separator) {
        if (original == null) {
            original = "";
        }
        StringBuilder stringBuilder = new StringBuilder(original);
        if (!original.isEmpty()) {
            stringBuilder.append(separator);
        }
        stringBuilder.append(newPart);
        return stringBuilder.toString();
    }
}
