import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/*
 * Võtab sisse kliendilt tulnud byte array ja kogused, mitu integeri ja mitu stringi seal sees olema peaks
 * võtab byte arrayst stringid ja integerid välja ning paneb need listidesse, millele saab getteritega ligi,
 * lisaks saab listidest kindlaid elemente getteritega kätte
 */
public class InputDeconstructor {
    byte[] input;
    int intNum;
    int strNum;

    List<String> strList = new ArrayList<>();
    List<Integer> intList = new ArrayList<>();

    public InputDeconstructor(byte[] input, int intNum, int strNum) {
        this.input = input;
        this.intNum = intNum;
        this.strNum = strNum;

        int cursor = 0;
        for (int i=0;i<intNum;i++){
            intList.add(ByteBuffer.wrap(input,cursor,4).getInt());
            cursor += 4;
        }
        for (int i=0;i<strNum;i++){
            int strLen = ByteBuffer.wrap(input,cursor,4).getInt();
            cursor += 4;
            strList.add(new String (input, cursor, strLen));
            cursor += strLen;
        }
        //System.out.println("Dekodeerisin sõnumi: "+intList.toString()+" , "+strList.toString());
        //System.out.println(getNthString(1));
    }

    public List<String> getStrList() {
        return strList;
    }
    public String getNthString(int n){
        return strList.get(n);
    }

    public List<Integer> getIntList() {
        return intList;
    }
    public int getNthInt (int n){
        return intList.get(n);
    }
}
