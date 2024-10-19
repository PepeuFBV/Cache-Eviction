import client.Client;
import compression.Compressor;
import service.Service;

public class Main {

    public static void main(String[] args) {
        Compressor compressor = new Compressor();
        Service service = new Service(compressor);
        Client client = new Client(service, compressor);
        client.startServices(true);
    }

}
