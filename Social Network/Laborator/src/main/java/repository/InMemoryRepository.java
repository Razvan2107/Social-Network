package repository;

import domain.Entity;
import domain.Friendship;
import domain.Utilizator;
import validator.ValidationException;
import validator.Validator;

import java.util.*;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id){
        if (id==null)
            throw new RepositoryException("ID-ul ar trebui sa nu fie null!");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public ArrayList<E> findAll() {
        ArrayList<E> valueList = new ArrayList<E>(entities.values());
        return valueList;
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity==null)
            throw new RepositoryException("Entitatea ar trebui sa nu fie vida!");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return Optional.of(entity);
        }
        else entities.put(entity.getId(),entity);
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id==null)
            throw new RepositoryException("Entitatea ar trebui sa nu fie vida!");
        Optional<E> toDelete = this.findOne(id);
        toDelete.ifPresent(e -> entities.remove(e.getId()));
        return toDelete;
    }

    @Override
    public Optional<E> update(E entity) throws ValidationException {
        validator.validate(entity);
        if(entities.containsKey(entity.getId())){
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        return Optional.of(entity);
    }
}
