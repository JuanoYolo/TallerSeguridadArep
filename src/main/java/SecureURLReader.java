import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SecureURLReader{
    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // Create a file and a password representation
        File trustStoreFile = new File("keystores/ecikeystore.p12");
        char[] trustStorePassword = "123456".toCharArray();
        // Load the trust store, the default type is "pkcs12", the alternative is "jks"
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        // Get the singleton instance of the TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        // Itit the TrustManagerFactory using the truststore object
        tmf.init(trustStore);

        //Set the default global SSLContext so all the connections will use it
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);

        readURL("https://localhost:5000/hello");
        // This one can't be read because the Java default truststore has been
        // changed.
        readURL("https://www.google.com");
    }

    public static void readURL(String url) throws MalformedURLException {
        URL site = new URL(url);
        try (BufferedReader reader
                       = new BufferedReader(new InputStreamReader(site.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                 System.out.println(inputLine);
            }
            } catch (IOException x) {
            System.err.println(x);
            }
    }

}

