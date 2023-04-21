package Utilities;

public class Utilities {
    public static byte[] cutBlanks(byte[] msg){
        var i = msg.length - 1;
        while(msg[i] == 0)
        {
            --i;
        }
        var temp = new byte[i + 1];
        System.arraycopy(msg, 0, temp, 0, i + 1);
        return temp;
    }

    public static String chatTime(){
        String date = java.time.LocalTime.now().toString();
        return date.substring(0, date.indexOf("."));
    }
}
