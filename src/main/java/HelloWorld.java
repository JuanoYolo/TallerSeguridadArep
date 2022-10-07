import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        port(5000);
        get("/hello", (req, res) -> "Hello World");
    }
}
