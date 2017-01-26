package coffee.machine.model.entity.product;

import coffee.machine.model.entity.Identified;

import static coffee.machine.config.CoffeeMachineConfig.DB_MONEY_COEFFICIENT;

/**
 * This class represents Parent class for product hierarchy.
 * Also, NOTE: this class represents drink's Addon.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Product implements Comparable<Product>, Identified {
    int id;
    String name;
    long price;
    int quantity;
    ProductType type;

    Product() {
    }

    public void incrementQuantityBy(int quantityToAdd) {
        quantity += quantityToAdd;
    }

    public void fillAbsentData(Product productData) {
        if ((id != productData.getId()) || (productData.getType() == null)) {
            throw new IllegalArgumentException();
        }
        if ((name == null) || (name.isEmpty())) {
            name = productData.getName();
        }
        if (price == 0) {
            price = productData.getPrice();
        }
        if (quantity == 0) {
            quantity = productData.getQuantity();
        }
    }

    public long getTotalPrice() {
        return price * quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public double getRealPrice() {
        return DB_MONEY_COEFFICIENT * price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public int compareTo(Product obj) {
        return Integer.compare(this.id, obj.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product that = (Product) o;

        if (type != that.type) return false;
        if (id != that.id) return false;
        if (price != that.price) return false;
        if (quantity != that.quantity) return false;
        if ((name == null) && (that.name == null)) {
            return true;
        } else {
            return name != null ? name.equals(that.name) : that.name == null;
        }

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + quantity;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                ", type=" + type +
                '}';
    }

    public static class Builder {
        private Product product;
        private ProductFactory productFactory = ProductFactory.getInstance();

        public Builder() {
            this.product = new Product();
            this.product.type = ProductType.ADDON;
        }

        public Builder(ProductType productType) {
            this.product = productFactory.getNewInstanceOfType(productType);
            this.product.type = productType;
        }

        public Builder setId(int id) {
            product.id = id;
            return this;
        }

        public Builder setName(String name) {
            product.name = name;
            return this;
        }

        public Builder setPrice(long price) {
            product.price = price;
            return this;
        }

        public Builder setQuantity(int quantity) {
            product.quantity = quantity;
            return this;
        }

        public Product build() {
            return product;
        }

    }

}
