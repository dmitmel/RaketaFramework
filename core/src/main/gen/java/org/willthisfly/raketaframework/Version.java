package org.willthisfly.raketaframework;

public class Version {
    private Version() {
        throw new UnsupportedOperationException("Can\'t create instance of Version");
    }


    public static String id() {
        return "1.4";
    }

    public static void main(String[] args) {
        System.out.println(id());
    }
}
