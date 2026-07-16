package com.codingShuttle.project.Lovable.dto.files;

import java.time.Instant;

public record FileNode(
        String filePath,
        Instant modifiesAt,
        Long size,
        String type
) {
}
