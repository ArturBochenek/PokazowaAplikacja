public class Transaction {
    private final String name;
    private final Double quantity;
    private final Double initialValue;
    private double currentValue;
    public Transaction(String name, double quantity, double initialValue) {
        this.name = name;
        this.quantity = quantity;
        this.initialValue = initialValue;
    }
    public String getName() {
        return name;
    }
    public Double getQuantity() {
        return quantity;
    }
    public Double getInitialValue() {
        return initialValue;
    }
    public double getCurrentValue() {
        return currentValue;
    }
    public void setCurrentvalue(double currentValue) {
        this.currentValue = currentValue;
    }

}
