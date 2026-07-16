package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query(""" 
            SELECT p FROM Project p WHERE p.deletedAt IS NULL AND EXISTS( 
                    SELECT 1 FROM ProjectMember pm WHERE pm.id.userId = :userId 
                    AND pm.id.projectId = p.id)
                                
                    ORDER BY p.updatedAt DESC
                                
            """
    )
    List<Project> findAllProjectOfUser(@Param("userId") Long userId);

    @Query(
            """
SELECT p FROM Project p WHERE p.id=:projectId AND p.deletedAt IS NULL 
AND EXISTS (SELECT 1 FROM ProjectMember pm 
WHERE pm.id.userId=:userId
AND pm.id.projectId=:projectId)
"""

    )
    Optional<Project> findProjectByIdAndUserId(@Param("userId") Long userId , @Param("projectId") Long projectId);
}
