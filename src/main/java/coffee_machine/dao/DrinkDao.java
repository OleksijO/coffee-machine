package coffee_machine.dao;

import coffee_machine.model.entity.goods.Drink;

import java.util.List;

public interface DrinkDao extends GenericDao<Drink> {

	@Override
	Drink insert(Drink drink);

	@Override
	void update(Drink drink);

	@Override
	List<Drink> getAll();

	@Override
	Drink getById(int id);

	@Override
	void deleteById(int id);

}