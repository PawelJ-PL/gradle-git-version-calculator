package com.github.gradle_git_version_calculator.models;

import java.util.Collections;
import java.util.List;

public class GitCommandResult {
    
    private final int statusCode;
    
    private final List<String> stdOut;
    
    private final List<String> stdErr;
    
    public GitCommandResult(int statusCode, List<String> stdOut, List<String> stdErr) {
        this.statusCode = statusCode;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public List<String> getStdOut() {
        return Collections.unmodifiableList(stdOut);
    }
    
    public List<String> getStdErr() {
        return Collections.unmodifiableList(stdErr);
    }
    
    public String getStdOutAsString() {
        return convertListToString(stdOut);
    }
    
    public String getStdErrAsString() {
        return convertListToString(stdErr);
    }
    
    private String convertListToString(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(line -> {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Status code: %d", statusCode));
        if (stdOut.size() > 0) {
            stringBuilder.append("\nStdout:\n");
            stringBuilder.append(getStdOutAsString());
        }
        if (stdErr.size() > 0) {
            stringBuilder.append("\nStderr:\n");
            stringBuilder.append(getStdErrAsString());
        }
        return stringBuilder.toString();
    }
}
