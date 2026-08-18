package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.ProjectFileMapper;
import com.codingShuttle.project.Lovable.Repository.ProjectFileRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectRepository;
import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.entity.ProjectFile;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectFileRepository projectFileRepository;

    private final ProjectRepository projectRepository;

    private final MinioClient minioClient;
    private final ProjectFileMapper projMapper;

    @Value("${minio.bucket}")
    private String projectBucket;

    @Override
    public List<FileNode> getFileTree( Long projectId) {
        List<ProjectFile> projectFile=projectFileRepository.findByProjectId(projectId).orElseThrow(()->new ResourceNotFoundException("projectFile",String.valueOf(projectId)));

        return projMapper.toFileNode(projectFile);
    }

    @Override
    public FileContentResponse getFileContent( Long projectId, String path) {
       String objectName=projectId + "/" + path;

       try{
           InputStream is=minioClient.getObject(GetObjectArgs.builder()
                   .bucket(projectBucket).object(objectName).build());

           String content=new String(is.readAllBytes(),StandardCharsets.UTF_8);
           return  new FileContentResponse(path,content);
       }
       catch (Exception e)
       {
            log.error("Failed to read file : {}/{}",projectId,path,e);
            throw new RuntimeException("Failed to read file content",e);
       }


    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}",filePath);

        Project project= projectRepository.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("project",String.valueOf(projectId)));

        String cleanPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        String objectKey = projectId + "/" +cleanPath;

        try {

            //convert it to byte stream
            //saving file content
            byte[] contentBytes= fileContent.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream=new ByteArrayInputStream(contentBytes);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream,contentBytes.length,-1)
                            .contentType(determineContentType(filePath))
                            .build());

            //saving file meta data
            ProjectFile file=projectFileRepository.findByProjectIdAndPath(projectId,cleanPath)
                    .orElseGet(()->ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .minioObjectKey(objectKey)
                            .createdAt(Instant.now())
                            .build());

            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);

        }
        catch (Exception e)
        {
            log.error("Failed to save file {}/{}",projectId,cleanPath,e);
            throw new RuntimeException("File save failed",e);
        }

        //save the file metadata in postgress
        //save file content in miniio
    }

    private String determineContentType(String path)
    {
        String type= URLConnection.guessContentTypeFromName(path);
        if(type!=null) return type;
        if(path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")) return "text/javascript";
        if(path.endsWith(".json")) return "application/json";
        if(path.endsWith(".css")) return "text/css";

        return "text/plain";
    }
}
