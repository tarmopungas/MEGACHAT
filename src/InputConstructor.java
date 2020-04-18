import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputConstructor {
    List<String> strList = new ArrayList<>();
    List<Integer> intList = new ArrayList<>();
    int strListSize = 0;

    public byte[] getOutput() {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream(intList.size() * 4 + strList.size() * 4 + strListSize);
            DataOutputStream dstream = new DataOutputStream(stream);
            for (Integer num : intList) {
                dstream.writeInt(num);
            }
            for (String str : strList) {
                dstream.writeInt(str.length());
                stream.write(str.getBytes());
            }
            dstream.close();
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to form request");
        }
    }

    public void insertStr(String str) {
        strList.add(str);
        strListSize += str.length();
    }

    public void insertInt(int num) {
        intList.add(num);
    }
}



