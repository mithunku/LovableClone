package com.codingShuttle.project.Lovable.dto.files;

import java.time.Instant;

public record FileNode(
        String path

) {
    @Override
    public String toString(){
        return path;
    }
}
