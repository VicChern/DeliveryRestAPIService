package com.softserve.itacademy.kek.mappers;

import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.ActorDto;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.impl.Actor;

/**
 * Interface for {@link Actor} mapping
 */
public interface IActorMapper {
    IActorMapper INSTANCE = Mappers.getMapper(IActorMapper.class);

    /**
     * Transform {@link IActor} to {@link ActorDto}
     *
     * @param actor actor
     * @return actorDto
     */
    ActorDto toActorDto (IActor actor);
}
