package sk.itsovy.projectSova7;

public class Main {

    public static void main(String[] args) {
        Database database = new Database();
        database.addCar(new Car("BMW", "red", 'g', "KE123AA", 22222));
        database.addCar(new Car("Audi", "pink", 'p', "KE555AA", 80222));
        database.generateXML();
        for (Car audi : database.getCarsByBrand("Audi")) {
            System.out.println(audi.getSpz());
        }
        for (Car audi : database.getCarsByFuel('p')) {
            System.out.println(audi.getSpz());
        }
        for (Car audi : database.getCarsByRegion("KE")) {
            System.out.println(audi.getSpz());
        }
        database.changeSPZ("KE555AA","KE555BB");

        for (Car audi : database.getCarsByPrice(30000)) {
            System.out.println(audi.getSpz());
        }
    }
}
