package movies.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import movies.domain.dtos.ActorDto;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Actor")
@Table(name = "actors")
public class Actor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Field
    private String name;

    @Column(name = "surname")
    @Field
    private String surname;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Movie> movies = new HashSet<>();

    public Actor() {
    }

    public Actor(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        return id != null && id.equals(((Actor) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public ActorDto toDto() {
        return new ActorDto(id, name, surname);
    }

}
