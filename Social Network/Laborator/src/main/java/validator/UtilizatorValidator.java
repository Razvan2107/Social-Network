package validator;

import domain.Entity;
import domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator>{
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getId()==null){
            throw new ValidationException("ID-ul nu poate sa fie null!");
        }
        if (entity.getFirstName() == null) {
            throw new ValidationException("Numele de familie nu poate sa fie null!");
        }
        if (entity.getLastName() == null) {
            throw new ValidationException("Prenumele nu poate sa fie null!");
        }
    }
}
