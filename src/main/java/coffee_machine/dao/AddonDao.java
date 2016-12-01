package coffee_machine.dao;

import coffee_machine.model.entity.item.Item;

import java.util.List;
import java.util.Set;

public interface AddonDao extends GenericDao<Item> {

	void updateQuantityAllInList(List<Item> items);

	void updateQuantity(Item drink);

	List<Item> getAllFromList(List<Item> addons);

	List<Item> getAllByIds(Set<Integer> itemIds);
}