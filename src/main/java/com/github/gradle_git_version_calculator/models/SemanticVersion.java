package com.github.gradle_git_version_calculator.models;

public class SemanticVersion {
    
    private Integer major;
    
    private Integer minor;
    
    private Integer patch;
    
    private String preRelease;
    
    private String buildMetadata;
    
    public SemanticVersion() {
    }
    
    public SemanticVersion(Integer major, Integer minor, Integer patch, String preRelease, String buildMetadata) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
        this.buildMetadata = buildMetadata;
    }
    
    public SemanticVersion(Integer major, Integer minor, Integer patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
}
