package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.Exceptions.NoNamesFoundError;
import com.github.gradle_git_version_calculator.Exceptions.NotGitRepositoryError;
import com.github.gradle_git_version_calculator.git_commands.DescribeCommand;
import com.github.gradle_git_version_calculator.git_commands.RevListCommand;
import com.github.gradle_git_version_calculator.git_commands.StatusCommand;
import com.github.gradle_git_version_calculator.models.GitCommandResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GitRepositoryTest {
    
    private GitRepository gitRepository;
    private DescribeCommand describeCommand;
    private RevListCommand revListCommand;
    private StatusCommand statusCommand;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Before
    public void setUp() {
        gitRepository = new GitRepository(prepareGitFactory());
    }
    
    private GitCommandsFactory prepareGitFactory() {
        GitCommandsFactory gitCommandsFactory = mock(GitCommandsFactory.class);
        describeCommand = mock(DescribeCommand.class);
        when(gitCommandsFactory.getDescribe()).thenReturn(describeCommand);
        setupDescribeCommand();
        revListCommand = mock(RevListCommand.class);
        when(gitCommandsFactory.getRevList()).thenReturn(revListCommand);
        setupRevListCommand();
        statusCommand = mock(StatusCommand.class);
        when(gitCommandsFactory.getStatus()).thenReturn(statusCommand);
        setupStatusCommand();
        return gitCommandsFactory;
    }
    
    private void setupDescribeCommand() {
        when(describeCommand.setTags(anyBoolean())).thenReturn(describeCommand);
        when(describeCommand.setAbbrev(anyInt())).thenReturn(describeCommand);
        when(describeCommand.setMatch(anyString())).thenReturn(describeCommand);
    }
    
    private void setupRevListCommand() {
        when(revListCommand.setCount(anyBoolean())).thenReturn(revListCommand);
        when(revListCommand.setSpec(anyString())).thenReturn(revListCommand);
    }
    
    private void setupStatusCommand() {
        when(statusCommand.setPorcelain(anyBoolean())).thenReturn(statusCommand);
    }
    
    @Test
    public void shouldReturnLatestTag() {
        //given
        when(describeCommand.call()).thenReturn(new GitCommandResult(0,
                                                                     Collections.singletonList("1.0.0"),
                                                                     Collections.emptyList()));
        
        //when
        Optional<String> result = gitRepository.getLatestTag();
        
        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo("1.0.0");
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
        verify(describeCommand, times(1)).setMatch("*");
    }
    
    @Test
    public void shouldReturnEmptyOnNotRepositoryError() {
        //given
        GitCommandResult commandResult = new GitCommandResult(128, Collections.emptyList(),
                                                              Collections.singletonList("Not git repository"));
        when(describeCommand.call()).thenThrow(new NotGitRepositoryError(commandResult));
        
        //when
        Optional<String> result = gitRepository.getLatestTag();
        
        //then
        assertThat(result.isPresent()).isFalse();
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
        verify(describeCommand, times(1)).setMatch("*");
    }
    
    @Test
    public void shouldReturnEmptyOnNoNamesFoundError() {
        //given
        GitCommandResult commandResult = new GitCommandResult(128, Collections.emptyList(),
                                                              Collections.singletonList("Not names found"));
        when(describeCommand.call()).thenThrow(new NoNamesFoundError(commandResult));
        
        //when
        Optional<String> result = gitRepository.getLatestTag();
        
        //then
        assertThat(result.isPresent()).isFalse();
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
        verify(describeCommand, times(1)).setMatch("*");
    }
    
    @Test
    public void shouldThrowExceptionWhenDescribeOutputMoreOutputLines() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Arrays.asList("First line", "second line"),
                                                              Collections.emptyList());
        when(describeCommand.call()).thenReturn(commandResult);
        
        //when
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 1 line, got 2:\nFirst line\nsecond line");
        gitRepository.getLatestTag();
        
        //then
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
    }
    
    @Test
    public void shouldThrowExceptionWhenDsecribeOutputNoOutputLine() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Collections.emptyList(),
                                                              Collections.emptyList());
        when(describeCommand.call()).thenReturn(commandResult);
        
        //when
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 1 line, got 0:");
        gitRepository.getLatestTag();
        
        //then
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
    }
    
    @Test
    public void shouldGetNumberOfCommitsSinceTag() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Collections.singletonList("7"),
                                                              Collections.emptyList());
        when(revListCommand.call()).thenReturn(commandResult);
        
        //when
        Integer result = gitRepository.getCommitsSinceTag("0.1.0");
        
        //then
        assertThat(result).isEqualTo(7);
        verify(revListCommand, times(1)).setCount(true);
        verify(revListCommand, times(1)).setSpec("0.1.0..");
    }
    
    @Test
    public void shouldThrowErrorWhenRevListOutputMultipleLine() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Arrays.asList("7", "10"),
                                                              Collections.emptyList());
        when(revListCommand.call()).thenReturn(commandResult);
        
        //when
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 1 line, got 2:\n7\n10");
        gitRepository.getCommitsSinceTag("0.1.0");
        
        //then
        verify(revListCommand, times(1)).setCount(true);
        verify(revListCommand, times(1)).setSpec("0.1.0..");
    }
    
    @Test
    public void shouldThrowErrorWhenRevListOutputNoLines() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Collections.emptyList(),
                                                              Collections.emptyList());
        when(revListCommand.call()).thenReturn(commandResult);
        
        //when
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected 1 line, got 0:");
        gitRepository.getCommitsSinceTag("0.1.0");
        
        //then
        verify(revListCommand, times(1)).setCount(true);
        verify(revListCommand, times(1)).setSpec("0.1.0..");
    }
    
    @Test
    public void shouldReturnTrueOnCleanRepository() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Collections.emptyList(),
                                                              Collections.emptyList());
        when(statusCommand.call()).thenReturn(commandResult);
        
        //when
        Boolean result = gitRepository.isClean();
        
        //then
        assertThat(result).isTrue();
        verify(statusCommand, times(1)).setPorcelain(true);
    }
    
    @Test
    public void shouldReturnFalseOnDirtyRepository() {
        //given
        GitCommandResult commandResult = new GitCommandResult(0, Collections.singletonList("??"),
                                                              Collections.emptyList());
        when(statusCommand.call()).thenReturn(commandResult);
        
        //when
        Boolean result = gitRepository.isClean();
        
        //then
        assertThat(result).isFalse();
        verify(statusCommand, times(1)).setPorcelain(true);
    }
    
    @Test
    public void shouldReturnLastPrefixedTag() {
        //given
        when(describeCommand.call()).thenReturn(new GitCommandResult(0,
                                                                     Collections.singletonList("xxx_1.0.0"),
                                                                     Collections.emptyList()));
    
        //when
        Optional<String> result = gitRepository.getLatestTag("xxx_");
    
        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo("xxx_1.0.0");
        verify(describeCommand, times(1)).setTags(true);
        verify(describeCommand, times(1)).setAbbrev(0);
        verify(describeCommand, times(1)).setMatch("xxx_*");
    }
    
}
