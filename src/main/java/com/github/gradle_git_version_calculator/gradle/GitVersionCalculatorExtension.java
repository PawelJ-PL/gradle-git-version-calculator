package com.github.gradle_git_version_calculator.gradle;

import com.github.gradle_git_version_calculator.GitCommandsFactory;
import com.github.gradle_git_version_calculator.GitRepository;
import com.github.gradle_git_version_calculator.GitVersionCalculator;
import org.gradle.api.Project;

public class GitVersionCalculatorExtension {
    
    private final Project project;
    
    private String prefix = "";
    
    public GitVersionCalculatorExtension(Project project) {
        this.project = project;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String calculateVersion(String prefix) {
        GitRepository repository = new GitRepository(new GitCommandsFactory(project.getProjectDir().getAbsolutePath()));
        GitVersionCalculator calculator = new GitVersionCalculator(repository);
        return calculator.calculateSemVer(prefix).toString();
    }
    
    public String calculateVersion() {
        return calculateVersion(prefix);
    }
}
