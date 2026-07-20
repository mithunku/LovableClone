package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getFileTree(Long userId, Long projectId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long userId, Long projectId, String path) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}",filePath);

        //save the file metadata in postgress
        //save file content in miniio
    }
}
