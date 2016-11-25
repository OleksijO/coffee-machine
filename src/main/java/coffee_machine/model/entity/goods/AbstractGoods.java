package coffee_machine.model.entity.goods;

import static coffee_machine.controller.Parameters.DB_MONEY_COEFF;

abstract public class AbstractGoods implements Comparable<AbstractGoods> {
    protected int id;
    protected String name;
    protected long price;
    protected int quantity;

    public AbstractGoods() {
    }

    public AbstractGoods(AbstractGoods goods) {
        id = goods.getId();
        name = goods.getName();
        price = goods.getPrice();
        quantity = goods.getQuantity();
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
        return DB_MONEY_COEFF * price;
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

    @Override
    public int compareTo(AbstractGoods obj) {
        return Integer.compare(this.id, obj.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractGoods that = (AbstractGoods) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (price ^ (price >>> 32));
        return result;
    }
}
