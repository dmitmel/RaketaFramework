package github.dmitmel.raketaframework;

public class Version {
    private Version() {
        throw new RuntimeException("Can\'t create instance of Version");
    }


    public static String id() {
        return "1.2";
    }

    public static void main(String[] args) {
        System.out.println(id());
    }
}
