package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile,Long> {

    Optional<ProjectFile> findByProjectIdAndPath(Long projectId,String cleanPath);

    Optional<List<ProjectFile>> findByProjectId(Long projectId);
}
