package com.example.microservice.common.repository;

import com.example.microservice.common.entity.BaseEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface BaseRepository<T extends BaseEntity> extends PanacheRepository<T> {

    /**
     * Encuentra todas las entidades activas
     */
    default java.util.List<T> findAllActive() {
        return list("isActive", true);
    }

    /**
     * Busca una entidad activa por ID
     */
    default T findByIdActive(Long id) {
        return find("id = ?1 and isActive = true", id).firstResult();
    }

    /**
     * Desactiva una entidad de forma lógica
     */
    default void deactivate(Long id) {
        update("isActive = false where id = ?1", id);
    }
}
