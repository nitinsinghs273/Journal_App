package com.journal.journalApp.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Ensuring username is unique and non-null
    private String username;

    private String email;
    private boolean sentimentalAnalysis;
    @Column(nullable = false)
    private String city;

    @Column(nullable = false) // Ensuring password is non-null
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @NonNull
    @ToString.Exclude
    //@JsonManagedReference // indicate managed side of the relationship
    private List<JournalEntry> journalEntries = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();



}
