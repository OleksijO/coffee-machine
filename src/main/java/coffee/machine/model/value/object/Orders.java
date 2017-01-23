package coffee.machine.model.value.object;

import coffee.machine.model.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oleksij.onysymchuk@gmail
 */
public class Orders {
    private List<Order> orders = new ArrayList<>();
    private int totalCount;

    public List<Order> getOrderList() {
        return orders;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Orders setOrders(List<Order> orders) {
        this.orders.addAll(orders);
        return this;
    }

    public Orders setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }
}
