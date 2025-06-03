package com.pm.unitalk.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "local_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(updatable = true)
    private String password;

    @CreationTimestamp
    private Date creationTime;
    @LastModifiedDate
    private Date lastUpdatedTime;

    @Column(name = "role")
    private String role = "USER"; // Default role

    @OneToMany(mappedBy = "localUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
