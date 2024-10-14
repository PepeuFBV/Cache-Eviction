import client.Client;
import service.Service;
import java.util.Scanner;
import entities.OS;

public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        client.startServices();
    }

}
