package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Repository.ProjectFileRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectRepository;
import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectFileRepository projectFileRepository;

    private final ProjectRepository projectRepository;

    private final MinioClient minioClient;

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

        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("project",String.valueOf(projectId)));

        String cleanPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        String objectKey = projectId + "/" +cleanPath;

        try {

        }
        catch (Exception e)
        {

        }
        //save the file metadata in postgress
        //save file content in miniio
    }
}
