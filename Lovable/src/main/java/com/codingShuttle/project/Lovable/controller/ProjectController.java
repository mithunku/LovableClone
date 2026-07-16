package com.codingShuttle.project.Lovable.controller;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.codingShuttle.project.Lovable.dto.project.ProjectRequest;
import com.codingShuttle.project.Lovable.dto.project.ProjectResponse;
import com.codingShuttle.project.Lovable.dto.project.ProjectSummaryResponse;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(){
        System.out.println("enter get all proejcte fro user");
        return ResponseEntity.ok(projectService.getUserProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){

        return ResponseEntity.ok(projectService.getUserProjectsById(id));
    }

    @PostMapping
    public  ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest request)
    {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,@RequestBody @Valid ProjectRequest request)
    {

        return  ResponseEntity.ok(projectService.updateProject(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectResponse> deleteProject(@PathVariable Long id)
    {

        projectService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
