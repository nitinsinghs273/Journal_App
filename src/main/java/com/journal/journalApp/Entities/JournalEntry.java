package com.journal.journalApp.Entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.journal.journalApp.Enum.Sentiment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "journals")
@Entity
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @NonNull
    private String title;

    private String author;
    private String content;
    private LocalDateTime date;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonBackReference //prevent infinite recursion
    @JsonIgnore
    @ToString.Exclude
    private User user;

}
