package com.github.gradle_git_version_calculator.models;

import com.github.gradle_git_version_calculator.Exceptions.VersionError;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticVersion {
    
    private Integer major;
    
    private Integer minor;
    
    private Integer patch;
    
    private String preRelease;
    
    private String buildMetadata;
    
    private final static String WITHOUT_LEADING_ZEROS_PATTERN =
            "[A-Za-z1-9-][A-Za-z0-9-]*(?:\\.[A-Za-z1-9-][A-Za-z0-9-]*)";
    
    private final static String WITH_LEADING_ZEROS_PATTERN = "[A-Za-z0-9-]*+(?:\\.[A-Za-z0-9-]*)";
    
    private final static String COMPONENTS_PATTERN =
            String.format("^(?<major>\\d+)\\.(?<minor>\\d+)\\.(?<patch>\\d+)(?:-(?<preRelease>%s)*)?(?:\\+(?<buildMetadata>%s*))?$",
                          WITHOUT_LEADING_ZEROS_PATTERN, WITH_LEADING_ZEROS_PATTERN);
    
    public SemanticVersion(Integer major, Integer minor, Integer patch, String preRelease, String buildMetadata) {
        Objects.requireNonNull(major, "Major version can't be null");
        Objects.requireNonNull(minor, "Minor version can't be null");
        Objects.requireNonNull(patch, "Patch version can't be null");
        validateParameterRegex(preRelease, WITHOUT_LEADING_ZEROS_PATTERN);
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
        this.buildMetadata = buildMetadata;
    }
    
    public SemanticVersion(Integer major, Integer minor, Integer patch) {
        this(major, minor, patch, null, null);
    }
    
    private void validateParameterRegex(String parameter, String regex) {
        if (parameter == null) {
            return;
        }
        regex = "^(?:" + regex + ")*$";
        Pattern compiledPattern = Pattern.compile(regex);
        Matcher matcher = compiledPattern.matcher(parameter);
        System.out.println(parameter);
        System.out.println(regex);
        System.out.println(matcher.matches());
    }
    
    public static SemanticVersion fromString(String version) {
        Pattern compiledPattern = Pattern.compile(COMPONENTS_PATTERN);
        Matcher matcher = compiledPattern.matcher(version);
        if (!matcher.find()) {
            throw new VersionError(String.format("%s is not valid semver", version));
        }
        Integer major = Integer.parseInt(matcher.group("major"));
        Integer minor = Integer.parseInt(matcher.group("minor"));
        Integer patch = Integer.parseInt(matcher.group("patch"));
        return new SemanticVersion(major, minor, patch, matcher.group("preRelease"), matcher.group("buildMetadata"));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticVersion)) return false;
        SemanticVersion that = (SemanticVersion) o;
        return Objects.equals(major, that.major) &&
                Objects.equals(minor, that.minor) &&
                Objects.equals(patch, that.patch) &&
                Objects.equals(preRelease, that.preRelease) &&
                Objects.equals(buildMetadata, that.buildMetadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, preRelease, buildMetadata);
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(major);
        stringBuilder.append(".");
        stringBuilder.append(minor);
        stringBuilder.append(".");
        stringBuilder.append(patch);
        if (preRelease != null) {
            stringBuilder.append("-");
            stringBuilder.append(preRelease);
        }
        if (buildMetadata != null) {
            stringBuilder.append("+");
            stringBuilder.append(buildMetadata);
        }
        return stringBuilder.toString();
    }
}
