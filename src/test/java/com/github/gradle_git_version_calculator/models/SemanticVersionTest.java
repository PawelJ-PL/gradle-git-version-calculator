package com.github.gradle_git_version_calculator.models;

import com.github.gradle_git_version_calculator.Exceptions.VersionError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class SemanticVersionTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void shouldCreateVersionFromComplexString() {
        //given
        String inputData = "3.17.4-beta12-3.4+1build17";
        
        //when
        SemanticVersion result = SemanticVersion.fromString(inputData);
        
        //then
        assertThat(result.toString()).isEqualTo(inputData);
        assertThat(result.getMajor()).isEqualTo(3);
        assertThat(result.getMinor()).isEqualTo(17);
        assertThat(result.getPatch()).isEqualTo(4);
        assertThat(result.getPreRelease().get()).isEqualTo("beta12-3.4");
        assertThat(result.getBuildMetadata().get()).isEqualTo("1build17");
    }
    
    @Test
    public void shouldCreateVersionByConstructor() {
        //given
        String expectedVersion = "3.17.4-beta12-3.4+1build17";
        
        //when
        SemanticVersion result = new SemanticVersion(3, 17, 4, "beta12-3.4", "1build17");
    
        //then
        assertThat(result.toString()).isEqualTo(expectedVersion);
        assertThat(result.getMajor()).isEqualTo(3);
        assertThat(result.getMinor()).isEqualTo(17);
        assertThat(result.getPatch()).isEqualTo(4);
        assertThat(result.getPreRelease().get()).isEqualTo("beta12-3.4");
        assertThat(result.getBuildMetadata().get()).isEqualTo("1build17");
    }
    
    @Test
    public void shouldCreateVersionFromRequiredParameters() {
        //given
        String inputData = "0.1.0";
        
        //when
        SemanticVersion result = SemanticVersion.fromString(inputData);
        
        //then
        assertThat(result.toString()).isEqualTo(inputData);
        assertThat(result.getMajor()).isEqualTo(0);
        assertThat(result.getMinor()).isEqualTo(1);
        assertThat(result.getPatch()).isEqualTo(0);
        assertThat(result.getPreRelease().isPresent()).isFalse();
        assertThat(result.getBuildMetadata().isPresent()).isFalse();
    }
    
    @Test
    public void shouldCreateVersionFromRequiredParametersAndPreRelease() {
        //given
        String inputData = "1.1.0-12-8.5.f";
        
        //when
        SemanticVersion result = SemanticVersion.fromString(inputData);
        
        //then
        assertThat(result.toString()).isEqualTo(inputData);
        assertThat(result.getMajor()).isEqualTo(1);
        assertThat(result.getMinor()).isEqualTo(1);
        assertThat(result.getPatch()).isEqualTo(0);
        assertThat(result.getPreRelease().get()).isEqualTo("12-8.5.f");
        assertThat(result.getBuildMetadata().isPresent()).isFalse();
    }
    
    @Test
    public void shouldCreateVersionFromRequiredParametersAndBuildMetadata() {
        //given
        String inputData = "0.1.0+04-1.abc-1";
        
        //when
        SemanticVersion result = SemanticVersion.fromString(inputData);
        
        //then
        assertThat(result.toString()).isEqualTo(inputData);
        assertThat(result.getMajor()).isEqualTo(0);
        assertThat(result.getMinor()).isEqualTo(1);
        assertThat(result.getPatch()).isEqualTo(0);
        assertThat(result.getPreRelease().isPresent()).isFalse();
        assertThat(result.getBuildMetadata().get()).isEqualTo("04-1.abc-1");
    }
    
    @Test
    public void shouldUpdateVersion() {
        //given
        String inputData = "3.17.4-beta12-3.4+1build17";
        SemanticVersion semanticVersion = SemanticVersion.fromString(inputData);
        
        //when
        semanticVersion.setMajor(4);
        semanticVersion.setMinor(19);
        semanticVersion.setPatch(7);
        semanticVersion.setPreRelease("Updated.1-a");
        semanticVersion.setBuildMetadata("new.Build-1");
        
        //then
        assertThat(semanticVersion.toString()).isEqualTo("4.19.7-Updated.1-a+new.Build-1");
    }
    
    @Test
    public void shouldCopyVersion() {
        //given
        String inputData = "3.17.4-beta12-3.4+1build17";
        SemanticVersion semanticVersion = SemanticVersion.fromString(inputData);
        
        //when
        SemanticVersion newVersion = semanticVersion.copy();
        semanticVersion.setMajor(4);
        semanticVersion.setMinor(19);
        semanticVersion.setPatch(7);
        semanticVersion.setPreRelease("Updated.1-a");
        semanticVersion.setBuildMetadata("new.Build-1");
        
        //then
        assertThat(semanticVersion.toString()).isEqualTo("4.19.7-Updated.1-a+new.Build-1");
        assertThat(newVersion.toString()).isEqualTo("3.17.4-beta12-3.4+1build17");
    }
    
    @Test
    public void shouldThrowExceptionWhenMajorNotNumber() {
        //given
        String inputData = "a3.17.4-beta12-3.4+1build17";
        
        //when
        expectedException.expect(VersionError.class);
        expectedException.expectMessage("a3.17.4-beta12-3.4+1build17 is not valid semver");
        SemanticVersion.fromString(inputData);
    }
    
    @Test
    public void shouldThrowExceptionWhenMinorNotNumber() {
        //given
        String inputData = "3.a17.4-beta12-3.4+1build17";
        
        //when
        expectedException.expect(VersionError.class);
        expectedException.expectMessage("3.a17.4-beta12-3.4+1build17 is not valid semver");
        SemanticVersion.fromString(inputData);
    }
    
    @Test
    public void shouldThrowExceptionWhenPatchNotNumber() {
        //given
        String inputData = "3.17.a4-beta12-3.4+1build17";
        
        //when
        expectedException.expect(VersionError.class);
        expectedException.expectMessage("3.17.a4-beta12-3.4+1build17 is not valid semver");
        SemanticVersion.fromString(inputData);
    }
    
    @Test
    public void shouldThrowExceptionWhenMajorNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Numeric element can't be negative");
        new SemanticVersion(-1, 3, 10);
    }
    
    @Test
    public void shouldThrowExceptionWhenMinorNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Numeric element can't be negative");
        new SemanticVersion(1, -3, 10);
    }
    
    @Test
    public void shouldThrowExceptionWhenPatchNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Numeric element can't be negative");
        new SemanticVersion(1, 3, -10);
    }
    
    @Test
    public void shouldThrowExceptionWhenMajorNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Numeric element can't be null");
        new SemanticVersion(null, 3, 10);
    }
    
    @Test
    public void shouldThrowExceptionWhenMinorNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Numeric element can't be null");
        new SemanticVersion(1, null, 10);
    }
    
    @Test
    public void shouldThrowExceptionWhenPatchNull() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Numeric element can't be null");
        new SemanticVersion(1, 3, null);
    }
    
    @Test
    public void shouldThrowExceptionWhenLeadingZeroInPreReleaseId() {
        //given
        String inputData = "0.2.4-12a.opa.01a";
        
        //when
        expectedException.expect(VersionError.class);
        expectedException.expectMessage("0.2.4-12a.opa.01a is not valid semver");
        SemanticVersion.fromString(inputData);
    }
    
}
