package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.goods.Addon;

public interface AddonDao extends GenericDao<Addon> {

	@Override
	int insert(Addon t);

	@Override
	void update(Addon t);

	@Override
	List<Addon> getAll();

	@Override
	Addon getById(int id);

	@Override
	void deleteById(int id);

}
