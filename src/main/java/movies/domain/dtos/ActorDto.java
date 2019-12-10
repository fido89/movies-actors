package movies.domain.dtos;

import movies.domain.Actor;

public class ActorDto {
    private Long id;
    private String name;
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
}
