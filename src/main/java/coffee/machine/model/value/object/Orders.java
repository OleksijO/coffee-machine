package coffee.machine.model.value.object;

import coffee.machine.model.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oleksij.onysymchuk@gmail
 */
public class Orders {
    private List<Order> orderList = new ArrayList<>();
    private int totalCount;

    public List<Order> getOrderList() {
        return orderList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public Orders setOrderList(List<Order> orders) {
        this.orderList.addAll(orders);
        return this;
    }

    public Orders setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public boolean isEmpty(){
        return orderList.isEmpty();
    }
}
