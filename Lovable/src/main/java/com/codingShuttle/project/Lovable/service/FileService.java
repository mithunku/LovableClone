package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long userId, Long projectId);

    FileContentResponse getFileContent(Long userId, Long projectId, String path);
}
