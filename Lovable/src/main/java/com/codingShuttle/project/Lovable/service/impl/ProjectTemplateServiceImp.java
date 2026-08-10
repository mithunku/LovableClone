package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Repository.ProjectFileRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectRepository;
import com.codingShuttle.project.Lovable.entity.Project;
import com.codingShuttle.project.Lovable.entity.ProjectFile;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.service.ProjectTemplateService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectTemplateServiceImp implements ProjectTemplateService {
    private final MinioClient minioClient;
    private final ProjectFileRepository projectFileRepository;
    private final ProjectRepository projRepo;

    private static final String TEMPLATE_BUCKET="starter-projects";
    private static final String TARGET_BUCKET="projects";
    private static final String TEMPLATE_NAME="react-vite-tailwind-daisyui-starter-main";
    @Override
    public void initilaizeProjectFromTemplate(Long projectId) {
        Project project = projRepo.findById(projectId).orElseThrow(()->{
           return new ResourceNotFoundException("project",projectId.toString());
        });

        try{
            Iterable<Result<Item>> results=minioClient.listObjects(
                    ListObjectsArgs.builder()
                                    .bucket(TEMPLATE_BUCKET)
                                    .prefix(TEMPLATE_NAME+"/")
                                    .recursive(true)
                                    .build()
            );

            //meta data store it in postgress
            List<ProjectFile> fileList =new ArrayList<>();

            for(Result<Item> result: results)
            {
                Item item= result.get();
                String sourceKey=item.objectName();

                //is removing the TEMPLATE_NAME/ prefix from the beginning of sourceKey.
                String cleanPath=sourceKey.replaceFirst(TEMPLATE_NAME + "/", "");

                //hrer this is destination path where we want to store the files
                String destKey = projectId + "/" +cleanPath;

                //here it is nothing but copying object frm starter template to the projects bucket
                //copying template file to actual project
                minioClient.copyObject(
                        CopyObjectArgs.builder().
                                bucket(TARGET_BUCKET)
                                        .object(destKey)
                                                .source(
                                                        CopySource.builder()
                                                        .bucket(TEMPLATE_BUCKET)
                                                                .object(sourceKey)
                                                                .build()
                                                ).
                                build()
                );

                ProjectFile pf=ProjectFile.builder()
                        .project(project)
                        .path(cleanPath)
                        .minioObjectKey(destKey)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();

                fileList.add(pf);


            }

            projectFileRepository.saveAll(fileList);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to initialize project from template");
        }

    }
}
