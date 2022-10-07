import static spark.Spark.*;

public class HelloRemote {
    public static void main(String[] args) {
        port(getPort());
        String keyStorePath1 = "keystores/Othersecure/ecikeystore.p12";
        String keyPasswd = "456789";
        secure("keystores/secure/ecikeystore.p12", "123456", null, null);
        get("/hellolocal", (req, res) -> "Hello Local 5001");
        get("/helloremoto", (req, res) -> {
            SecureURLReader secureURLReader = new SecureURLReader();
            secureURLReader.trust(keyStorePath1, keyPasswd);
            String resp = SecureURLReader.readURL("https://localhost:5010/hellolocal");
            return resp;
        });
    }
        static int getPort() {
            if (System.getenv("PORT") != null) {
                return Integer.parseInt(System.getenv("PORT"));
            }
            return 5010; //returns default port if heroku-port isn't set (i.e. on localhost)
        }

}
