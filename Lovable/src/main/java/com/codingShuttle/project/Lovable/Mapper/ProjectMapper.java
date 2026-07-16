package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.project.ProjectResponse;
import com.codingShuttle.project.Lovable.dto.project.ProjectSummaryResponse;
import com.codingShuttle.project.Lovable.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") //here we are specifying which framework we are using
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);
    ProjectSummaryResponse toProjectSummaryResponse(Project project);
}
