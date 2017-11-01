package com.github.gradle_git_version_calculator.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GitVersionCalculatorPlugin implements Plugin<Project> {
    
    @Override
    public void apply(Project project) {
        project.getExtensions().create("gitVersionCalculator", GitVersionCalculatorExtension.class, project);
        project.getTasks().create("printGitVersion", GitVersionTaskPrint.class);
    }
}
