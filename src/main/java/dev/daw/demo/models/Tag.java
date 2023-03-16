package dev.daw.demo.models;

import dev.daw.demo.models.Question;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tag {


    @Getter
    @Setter
    @Column(name = "name")
    @Id
    private String name;


    @ManyToMany(mappedBy = "tags")
    private List<Question> questions;

    public Tag(String s){
        this.name = s;
    }

}
