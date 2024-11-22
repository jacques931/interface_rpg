package rpg.items;

public abstract class Item {
    protected int price;
    protected String name;
    protected String imageSrc;
    protected int quantity;

    public Item(String name, String imageSrc, int price){
        this.price = price;
        this.name = name;
        this.imageSrc = imageSrc;
        this.quantity = 1;
    }

    public String getImageSrc(){return this.imageSrc;}

    public int getPrice() {
        return this.price;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity){
        this.quantity += quantity;
    }

    public void resetQuantity(){
        this.quantity = 1;
    }

    public String getName(){
        return this.name;
    }

    public String getTooltipDescription(){
        return "<html>" +
                "<b>" + this.name + "</b><br>" + // Nom en gras
                "Price: " + this.price + " gold<br>" + // Prix
                this.getDescription() +               // Description
                "</html>";
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return this.name + " - prix : " + this.price;
    }
}
