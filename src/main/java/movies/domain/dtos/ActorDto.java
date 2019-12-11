package movies.domain.dtos;

import movies.domain.Actor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class ActorDto {
    private Long id;
    @NotNull
    @Size(min = 1, max = 250)
    private String name;
    @Size(max = 250)
    private String surname;

    public ActorDto() {
    }

    public ActorDto(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Actor toActor() {
        return new Actor(id, name, surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }
}
