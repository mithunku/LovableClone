package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFileContent(Long userId, Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
