package com.codingShuttle.project.Lovable.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "Projects",

        indexes = {
                @Index(name = "index_on_deletedAt_updatedAt" ,columnList = "updatedAt DESC, deleted_At"),
                @Index(name = "index_deleted_At" ,columnList = "deleted_At")
        }


)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    String name;



    Boolean isPublic=false;
    @CreationTimestamp
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;


    Instant deletedAt;
}
