package coffee_machine.service;

import coffee_machine.model.entity.goods.Addon;

import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface AddonService {



    List<Addon> getAll();

    void refill(Map<Integer, Integer> quantitiesById);

}
