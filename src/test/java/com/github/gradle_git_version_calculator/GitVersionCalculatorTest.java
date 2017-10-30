package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.models.SemanticVersion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GitVersionCalculatorTest {
    
    private GitVersionCalculator service;
    private GitRepository gitRepository;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp()  {
        gitRepository = mock(GitRepository.class);
        service = new GitVersionCalculator(gitRepository);
    }
    
    @Test
    public void shouldGetCalculatedVersionWithDistanceAndDirtyRepo() {
        //given
        String originalTag = "3.1.12-8";
        when(gitRepository.getLatestTag("")).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(6);
        when(gitRepository.isClean()).thenReturn(false);
        
        //when
        SemanticVersion result = service.calculateSemVer();
        
        //then
        assertThat(result.toString()).isEqualTo("3.1.12-8+6.dev");
    }
    
    @Test
    public void shouldGetDefaultVersionWhenNoTagFound() {
        //given
        when(gitRepository.getLatestTag("")).thenReturn(Optional.empty());
        
        //when
        SemanticVersion result = service.calculateSemVer();
        
        //then
        assertThat(result.toString()).isEqualTo("0.0.1-SNAPSHOT");
        verify(gitRepository, never()).getCommitsSinceTag(anyString());
        verify(gitRepository, never()).isClean();
    }
    
    @Test
    public void shouldGetCalculatedVersionDirtyRepo() {
        //given
        String originalTag = "3.1.12-8";
        when(gitRepository.getLatestTag("")).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(0);
        when(gitRepository.isClean()).thenReturn(false);
        
        //when
        SemanticVersion result = service.calculateSemVer();
        
        //then
        assertThat(result.toString()).isEqualTo("3.1.12-8+dev");
    }
    
    @Test
    public void shouldGetCalculatedVersionWithDistance() {
        //given
        String originalTag = "3.1.12-8";
        when(gitRepository.getLatestTag("")).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(6);
        when(gitRepository.isClean()).thenReturn(true);
        
        //when
        SemanticVersion result = service.calculateSemVer();
        
        //then
        assertThat(result.toString()).isEqualTo("3.1.12-8+6");
    }
    
    @Test
    public void shouldGetCalculatedVersionWithDistanceAndDirtyRepoWhenNoPreviousBuildMetadata() {
        //given
        String originalTag = "3.1.12";
        when(gitRepository.getLatestTag("")).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(6);
        when(gitRepository.isClean()).thenReturn(false);
        
        //when
        SemanticVersion result = service.calculateSemVer();
        
        //then
        assertThat(result.toString()).isEqualTo("3.1.12+6.dev");
    }
    
    @Test
    public void shouldGetCalculatedVersionWithDistanceAndDirtyRepoWithPrefix() {
        //given
        String originalTag = "xyz__3.1.12-8";
        String prefix = "xyz__";
        when(gitRepository.getLatestTag(prefix)).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(6);
        when(gitRepository.isClean()).thenReturn(false);
        
        //when
        SemanticVersion result = service.calculateSemVer(prefix);
        
        //then
        assertThat(result.toString()).isEqualTo("3.1.12-8+6.dev");
    }
    
    @Test
    public void shouldThrowExceptionWhenTagDosntStartWithPrefix() {
        //given
        String originalTag = "xyz__3.1.12-8";
        String prefix = "abc";
        when(gitRepository.getLatestTag(prefix)).thenReturn(Optional.of(originalTag));
        when(gitRepository.getCommitsSinceTag(originalTag)).thenReturn(6);
        when(gitRepository.isClean()).thenReturn(false);
        
        //when
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("abc is not prefix of xyz__3.1.12-8");
        SemanticVersion result = service.calculateSemVer(prefix);
    }
}
