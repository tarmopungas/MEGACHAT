import java.io.*;

// Kasutaja objektide liigutamise hõlbustamiseks mõeldud meetodid

public class ObjectConversion {
    public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        // Teeb baitide massiivist objekti
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
    public static byte[] convertToBytes(Object object) throws IOException {
        // Teeb objektist baitide massiivi
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
    public static Kasutaja loeKasutaja(File file) {
        // Loeb antud failist Kasutaja isendi
        try (FileInputStream fi = new FileInputStream(file);
             ObjectInputStream oi = new ObjectInputStream(fi))
        {
            return (Kasutaja) oi.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
    public static void kirjutaKasutaja(File file, Kasutaja kasutaja) {
        // Kirjutab antud faili antud Kasutaja isendi
        try (FileOutputStream f = new FileOutputStream(file);
             ObjectOutputStream o = new ObjectOutputStream(f)) {
            o.writeObject(kasutaja);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
}
