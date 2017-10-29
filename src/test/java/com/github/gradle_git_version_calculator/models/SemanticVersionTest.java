package com.github.gradle_git_version_calculator.models;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SemanticVersionTest {
    
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
    
}
