package com.codingShuttle.project.Lovable.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Users")
@Builder
public class User implements UserDetails {

     @Id
             @GeneratedValue(strategy = GenerationType.AUTO)
     Long id;
     String username;
     String password;
     String name;
     String avatarUrl;

     @Column(unique = true)
     String stripeCustomerId;

//     @OneToMany(mappedBy = "owner")
//     List<Project> project;

     @CreationTimestamp
     Instant createdAt;
     @UpdateTimestamp
     Instant updatedAt;
     Instant deletedAt;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of();
     }
}
