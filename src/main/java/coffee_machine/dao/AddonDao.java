package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.goods.Addon;

public interface AddonDao extends GenericDao<Addon> {

	List<Addon> getAllFromList(List<Addon> addons);

	void updateAllInList(List<Addon> addons);

	List<Addon> getAllByIds(List<Integer> addonIds);

	void updateQuantity(Addon addon);
}
