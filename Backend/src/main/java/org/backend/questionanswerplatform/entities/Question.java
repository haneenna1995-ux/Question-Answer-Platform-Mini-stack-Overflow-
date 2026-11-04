package org.backend.questionanswerplatform.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String title;
    @Column(nullable = false, columnDefinition = "Text")
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User author;

    @CreatedDate
    private LocalDateTime createdAt= LocalDateTime.now();

    @ManyToMany
    @JoinTable(
            name="question_tag",
            joinColumns = @JoinColumn(name="question_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers= new HashSet<>();


    public Question() {
    }
    public Question(String title, String description, User author,  Set<Tag> tags)
    {
         this.title=title;
         this.description=description;
         this.author=author;
          this.tags=tags;
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author=author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
