package coffee_machine.dao;

import coffee_machine.model.entity.goods.Addon;

import java.util.List;

public interface AddonDao extends GenericDao<Addon> {

	@Override
	Addon insert(Addon addon);

	@Override
	void update(Addon addon);

	@Override
	List<Addon> getAll();

	@Override
	Addon getById(int id);

	@Override
	void deleteById(int id);

}
