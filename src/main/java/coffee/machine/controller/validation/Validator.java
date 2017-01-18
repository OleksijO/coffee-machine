package coffee.machine.controller.validation;

/**
 * Created by oleksij.onysymchuk@gmail on 18.01.2017.
 */
public interface Validator<T> {
    Notification validate(T object);
}
