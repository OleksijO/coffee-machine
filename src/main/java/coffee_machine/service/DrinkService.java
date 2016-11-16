package coffee_machine.service;


import coffee_machine.model.entity.goods.Drink;

import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface DrinkService {

    Drink create(Drink drink);

    void update(Drink drink);

    List<Drink> getAll();

    Drink getById(int id);

    void delete(int id);

    void refill(Map<Integer, Integer> quantitiesById);
}
