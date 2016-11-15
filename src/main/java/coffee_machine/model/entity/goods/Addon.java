package coffee_machine.model.entity.goods;

public class Addon extends AbstractGoods {

    public Addon() {
    }

    public Addon(Addon addon) {
        super(addon);
    }

    @Override
    public String toString() {
        return (quantity == 0) ? "" : "Addon [" + name + ", " + quantity + " pcs]";
    }

}
