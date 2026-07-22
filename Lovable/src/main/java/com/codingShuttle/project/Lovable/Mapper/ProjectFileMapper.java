package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.files.FileNode;
import com.codingShuttle.project.Lovable.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") //here we are specifying which framework we are using
public interface ProjectFileMapper {
    List<FileNode> toFileNode(List<ProjectFile> projectFile);
}
