package coffee.machine.model.entity.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 06.01.2017.
 */
public class Addons {
    private List<Item> addons = new ArrayList<>();

    public Addons add(List<Item> addons){
       this.addons.addAll(addons);
        return this;
    }

    public void incrementQuantities(List<Item> addonsToAdd){
        for(Item addon: addonsToAdd){
            addons.stream()
                    .filter(item->item.getId()==item.getId())
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)
                    .incrementQuantityBy(addon.getQuantity());
        }
    }


    public List<Item> getAddons() {
        return addons;
    }
}
