package com.codingShuttle.project.Lovable.controller;

import com.codingShuttle.project.Lovable.dto.files.FileContentResponse;
import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private final ProjectFileService fileService;
    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId)
    {
        Long userId=1L;
        return ResponseEntity.ok(fileService.getFileTree(userId,projectId));
    }

    @GetMapping("/{*path}") //why * is added because we can get entire path after // example /src/path/basic.jsx
    public ResponseEntity<FileContentResponse> getFileNode(@PathVariable Long projectId,@PathVariable String path)
    {
        Long userId=1L;
        return ResponseEntity.ok(fileService.getFileContent(userId,projectId,path));

    }
}
