package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AddonDao;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the implementation of Addon entity DAO
 * <p>
 * Mainly this is an adaptor for ProductDaoHelper, because Addon is Product,
 * but extends it's functionality with some methods
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AddonDaoImpl extends AbstractDao<Product> implements AddonDao {
    private ProductDaoHelper productDaoHelper;

    AddonDaoImpl(Connection connection) {
        super(connection);
        productDaoHelper = new ProductDaoHelper(connection);
    }


    @Override
    public Product insert(Product product) {
        return productDaoHelper.insert(product);
    }

    @Override
    public void updateQuantity(Product product) {
        productDaoHelper.updateQuantity(product);
    }

    @Override
    public List<Product> getAllFromList(List<Product> addonsToGet) {

        return productDaoHelper.getAllByIds(addonsToGet.stream()
                .map(Product::getId)
                .collect(Collectors.toSet()));
    }

    @Override
    public List<Product> getAllByIds(Set<Integer> productIds) {
        return productDaoHelper.getAllByIds(productIds);
    }

    @Override
    public void update(Product product) {
        productDaoHelper.update(product);
    }

    @Override
    public List<Product> getAll() {
        return productDaoHelper.getAll(ProductType.ADDON);
    }

    @Override
    public Optional<Product> getById(int id) {
        return productDaoHelper.getById(id);
    }

    @Override
    public void deleteById(int id) {
        productDaoHelper.deleteById(id);
    }

    @Override
    public void updateQuantityAllInList(List<Product> products) {
        productDaoHelper.updateQuantityAllInList(products);
    }

}
