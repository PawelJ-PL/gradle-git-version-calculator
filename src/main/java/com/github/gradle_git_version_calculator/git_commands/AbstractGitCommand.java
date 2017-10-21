package com.github.gradle_git_version_calculator.git_commands;

public abstract class AbstractGitCommand {

    protected String perform(String cwd, String... args) {
        return "";
    }

}
