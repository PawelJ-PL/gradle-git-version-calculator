package com.github.gradle_git_version_calculator;

import com.github.gradle_git_version_calculator.Exceptions.NoNamesFoundError;
import com.github.gradle_git_version_calculator.Exceptions.NotGitRepositoryError;
import com.github.gradle_git_version_calculator.git_commands.DescribeCommand;
import com.github.gradle_git_version_calculator.models.GitCommandResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GitRepositoryTest {
    
    private GitRepository gitRepository;
    private DescribeCommand describeCommand;
    
    @Before
    public void setUp() {
        gitRepository = new GitRepository(prepareGitFactory());
    }
    
    private GitCommandsFactory prepareGitFactory() {
        GitCommandsFactory gitCommandsFactory = mock(GitCommandsFactory.class);
        describeCommand = mock(DescribeCommand.class);
        when(gitCommandsFactory.getDescribe()).thenReturn(describeCommand);
        setupDescribeCommand();
        return gitCommandsFactory;
    }
    
    private void setupDescribeCommand() {
        when(describeCommand.setTags(anyBoolean())).thenReturn(describeCommand);
        when(describeCommand.setAbbrev(anyInt())).thenReturn(describeCommand);
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
    }
    
}
