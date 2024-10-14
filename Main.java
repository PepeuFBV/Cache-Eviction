import client.Client;
import service.Service;

public class Main {

    public static void main(String[] args) {
        Service service = new Service();
        Client client = new Client(service);
        client.startServices();
    }

}
