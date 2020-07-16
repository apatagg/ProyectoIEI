package sample.integracion;

public class Mobile {
    public String name;
    public String price;
    private String discount;
    private String shop;
    
    public Mobile(){
        
    }
    
    public Mobile(String name, String price, String discount, String shop){
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.shop = shop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price+"€";
    }

    public void setPrice(String price) {
        this.price = price+"€";
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String toString(){
        return "Modelo: " + this.name + "\n" + "Precio: " + this.price + "\nDescuento: " +this.discount;
    }
}
