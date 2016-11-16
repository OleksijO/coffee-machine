package coffee_machine.service;

import coffee_machine.model.entity.goods.Addon;

import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface AddonService {
    Addon create(Addon addon);

    void update(Addon addon);

    List<Addon> getAll();

    Addon getById(int id);

    void delete(int id);

    void refill(Map<Integer, Integer> quantitiesById);

}
