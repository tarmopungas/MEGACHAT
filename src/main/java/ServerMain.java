import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

// TODO: Custom passwords for protecting keys

public class ServerMain {
    /*MIT License
    Copyright (c) 2017 johndavidbustard

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    */
    public static SSLServerSocketFactory makeSSLSocketFactory(KeyStore loadedKeyStore, KeyManager[] keyManagers)
    {
        SSLServerSocketFactory res = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(loadedKeyStore);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
            res = ctx.getServerSocketFactory();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            //throw new IOException(e.getMessage());
        }
        return res;
    }
    public static SSLServerSocketFactory makeSSLSocketFactory(
            KeyStore loadedKeyStore, KeyManagerFactory loadedKeyFactory) {
        try {
            return makeSSLSocketFactory(loadedKeyStore, loadedKeyFactory.getKeyManagers());
        } catch (Exception e) {
            System.out.println(e.toString());
            //throw new IOException(e.getMessage());
        }
        return null;
    }
    public static SSLServerSocketFactory makeSSLSocketFactory(String keyAndTrustStoreClasspathPath, char[] passphrase)
    {
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            File keystrorefile = new File(keyAndTrustStoreClasspathPath);
            System.out.println(keystrorefile.getAbsolutePath());
            InputStream keystoreStream = new FileInputStream(keystrorefile);

            keystore.load(keystoreStream, passphrase);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, passphrase);
            return makeSSLSocketFactory(keystore, keyManagerFactory);
        } catch (Exception e) {
            System.out.println("Viga võtme lugemisega!");
            System.out.println(e.toString());
        }
        return null;
    }
    // End of code with Copyright (c) 2017 johndavidbustard

    public static void main(String[] args) throws Exception {
        // Loome uut KeyStore, kui sellist veel ei ole
        File f = new File("top-secret");

        if (!f.isFile()) {
            // TODO: Generate key
            System.out.println("Ei leidnud võtme. Palun tehke uut võtmet, nagu on kirjeldatud README-s.");
//            System.out.println("Ei leidnud võtme, loon uut. Palun oodake.");
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//            // GENERATING
//
//            char[] password = "123abc".toCharArray();
//            ks.load(null, password);
//            try (FileOutputStream fos = new FileOutputStream(f)) {
//                ks.store(fos, password);
//                System.out.println("Võti salvestatud faili" + "top-secret");
//                System.out.println("ÄRA KUNAGI JAGA SEE FAIL!!!");
//            }
        }

        SSLServerSocketFactory sslServerSocketFactory =
                makeSSLSocketFactory("top-secret" ,"123abc".toCharArray());
        assert sslServerSocketFactory != null;
        try (SSLServerSocket ss = (SSLServerSocket)
                sslServerSocketFactory.createServerSocket(1337)) {
            List<Thread> pool = new ArrayList<>();
            int threadNum = 100;
            for (int i = 0; i < threadNum; i++) {
                pool.add(new Thread(new Kuulaja(ss, i)));
                pool.get(pool.size() - 1).start();
            }

            System.out.println("Server töötab!");

            for (Thread thread : pool) {
                thread.join();
            }
        }
    }
}
