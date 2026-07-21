package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile,Long> {
}
