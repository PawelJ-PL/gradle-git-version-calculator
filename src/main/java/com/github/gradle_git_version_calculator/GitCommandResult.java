package com.github.gradle_git_version_calculator;

import java.util.stream.Stream;

public class GitCommandResult {
    
    private int statusCode;
    
    private Stream<String> stdOut;
    
    private Stream<String> stdErr;
    
    public GitCommandResult(int statusCode, Stream<String> stdOut, Stream<String> stdErr) {
        this.statusCode = statusCode;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public Stream<String> getStdOut() {
        return stdOut;
    }
    
    public Stream<String> getStdErr() {
        return stdErr;
    }
    
    public String getStdOutAsString() {
        return convertStreamToString(stdOut);
    }
    
    public String getStdErrAsString() {
        return convertStreamToString(stdErr);
    }
    
    private String convertStreamToString(Stream<String> stream) {
        StringBuilder stringBuilder = new StringBuilder();
        stream.forEachOrdered(line -> {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }
    
}
