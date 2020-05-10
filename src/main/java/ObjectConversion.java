import com.fasterxml.jackson.databind.ObjectMapper;

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
        try (FileInputStream f = new FileInputStream(file);
             DataInputStream in = new DataInputStream(new BufferedInputStream(f))) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(in.readUTF(), Kasutaja.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
    public static void kirjutaKasutaja(File file, Kasutaja kasutaja) {
        // Kirjutab antud faili antud Kasutaja isendi
        ObjectMapper objectMapper = new ObjectMapper();

        try (FileOutputStream f = new FileOutputStream(file);
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(f))) {
            String kasutajaAsJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(kasutaja);
            out.writeUTF(kasutajaAsJson);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
    public static Vestlusruum loeVestlusruum(File file) {
        // Loeb antud failist Vestlusruumi isendi
        try (FileInputStream f = new FileInputStream(file);
             DataInputStream in = new DataInputStream(new BufferedInputStream(f))) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(in.readUTF(), Vestlusruum.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
    public static void kirjutaVestlusruum(File file, Vestlusruum vestlusruum) {
        // Kirjutab antud faili antud Vestlusruumi isendi
        ObjectMapper objectMapper = new ObjectMapper();

        try (FileOutputStream f = new FileOutputStream(file);
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(f))) {
            String kasutajaAsJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(vestlusruum);
            out.writeUTF(kasutajaAsJson);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }
    }
}
