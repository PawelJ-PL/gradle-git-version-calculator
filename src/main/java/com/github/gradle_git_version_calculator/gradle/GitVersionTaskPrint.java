package com.github.gradle_git_version_calculator.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

public class GitVersionTaskPrint extends DefaultTask {
    
    private Project project;
    private GitVersionCalculatorExtension properties;
    
    public GitVersionTaskPrint() {
        project = getProject();
        properties = project.getExtensions().getByType(GitVersionCalculatorExtension.class);
    }
    
    @TaskAction
    void printVersion() {
        System.out.println(properties.calculateVersion());
    }
    
}
