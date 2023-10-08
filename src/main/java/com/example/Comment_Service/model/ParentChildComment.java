package com.example.Comment_Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parent_child_comment")
public class ParentChildComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Comment parentComment;
    @ManyToOne
    Comment childComment;

    int depth;
}
