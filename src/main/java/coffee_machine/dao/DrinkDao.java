package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.goods.Drink;

public interface DrinkDao extends GenericDao<Drink> {

	@Override
	int insert(Drink t);

	@Override
	void update(Drink t);

	@Override
	List<Drink> getAll();

	@Override
	Drink getById(int id);

	@Override
	void deleteById(int id);

}