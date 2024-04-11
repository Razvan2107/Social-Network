package service;

import domain.Entity;
import domain.Friendship;
import domain.Utilizator;
import validator.ValidationException;
import validator.Validator;
import repository.Repository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * operations service interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */

public interface ServiceInterface<ID,E extends Entity<ID>> {
    /**
     * add the entity with specified id, firstName, lastName
     * @param id
     *          id must be not null
     * @param firstName
     *          firstName must be not null
     * @param lastName
     *          lastName must be not null
     * @throws ValidationException
     *          if the entity is not valid
     * @throws IllegalArgumentException
     *          if the given entity is null.
     */
    Optional<Utilizator> add_user(ID id, String firstName, String lastName);

    /**
     * removes the entity with the specified id
     * @param id
     *      id must be not null
     * @throws IllegalArgumentException
     *      if the given id is null.
     */
    Optional<Utilizator> delete_user(ID id);

    /**
     * add a friendship
     * @param id1
     *      id1 must be not null
     * @param id2
     *      id2 must be not null
     */
    Optional<Friendship> add_friend(String id1, String id2);

    /**
     * delete a friendship
     * @param id1
     *      id must be not null
     * @param id2
     *      id must be not null
     */
    boolean delete_friend(String id1,String id2);
}
