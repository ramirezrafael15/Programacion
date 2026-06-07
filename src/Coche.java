public class Coche {
    private int id;
    private String marca;
    private int anio;
    private String color;

    public Coche(int id, String marca, int anio, String color) {
        this.id    = id;
        this.marca = marca;
        this.anio  = anio;
        this.color = color;
    }

    public Coche(String marca, int anio, String color) {
        this.marca = marca;
        this.anio  = anio;
        this.color = color;
    }

    public int getId()             { return id; }
    public void setId(int id)      { this.id = id; }

    public String getMarca()            { return marca; }
    public void setMarca(String marca)  { this.marca = marca; }

    public int getAnio()               { return anio; }
    public void setAnio(int anio)      { this.anio = anio; }

    public String getColor()            { return color; }
    public void setColor(String color)  { this.color = color; }
}
