package com.example.microservice.common.service;

import com.example.microservice.common.entity.BaseEntity;
import com.example.microservice.common.exception.AppException;
import com.example.microservice.common.exception.ErrorCode;
import com.example.microservice.common.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class BaseService<T extends BaseEntity, R extends BaseRepository<T>> {

    protected R repository;

    public T create(T entity) {
        log.debug("Creating entity of type {}", entity.getClass().getSimpleName());
        try {
            repository.persistAndFlush(entity);
            return entity;
        } catch (Exception e) {
            log.error("Error creating entity", e);
            throw new AppException(
                    ErrorCode.DATABASE_ERROR,
                    500,
                    "Error al crear la entidad: " + e.getMessage()
            );
        }
    }

    public T update(T entity) {
        log.debug("Updating entity of type {}", entity.getClass().getSimpleName());
        try {
            return repository.getEntityManager().merge(entity);
        } catch (Exception e) {
            log.error("Error updating entity", e);
            throw new AppException(
                    ErrorCode.DATABASE_ERROR,
                    500,
                    "Error al actualizar la entidad: " + e.getMessage()
            );
        }
    }

    public void delete(Long id) {
        log.debug("Deleting entity with id {}", id);
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            log.error("Error deleting entity", e);
            throw new AppException(
                    ErrorCode.DATABASE_ERROR,
                    500,
                    "Error al eliminar la entidad: " + e.getMessage()
            );
        }
    }

    public T findById(Long id) {
        log.debug("Finding entity with id {}", id);
        return repository.findByIdOptional(id)
                .orElseThrow(() -> new AppException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        404,
                        "Recurso no encontrado con id: " + id
                ));
    }

    public List<T> findAll() {
        log.debug("Finding all entities");
        return repository.listAll();
    }

    public List<T> findAllActive() {
        log.debug("Finding all active entities");
        return repository.findAllActive();
    }

    public void deactivate(Long id) {
        log.debug("Deactivating entity with id {}", id);
        repository.deactivate(id);
    }
}
