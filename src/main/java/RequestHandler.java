import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;


public class RequestHandler implements Runnable {

    private Socket socket;

    RequestHandler(Socket socket) {
        this.socket = socket;
    }


    private void sendAnswer(String answer) throws IOException{
        new PrintStream(socket.getOutputStream(), true, "UTF-8").println(answer);
    }


    private String generateAnswer(int year) {
       JSONObject answer = new JSONObject();
        int code = (year < 0) ? 400 : 200;
        String message =
                (year == -1) ?
                        "invalid parameter" :
                        (year == -2) ? "invalid year" :
                        (year % 4 != 0 || year % 100 == 0 && year % 400 != 0) ?
                                "13/09/" + year%100 :
                                "14/09/" + year%100;

       answer.put("errorCode", code);
       answer.put("dataMessage", message);

        return StringEscapeUtils.unescapeJava(answer.toJSONString());
    }


    private int getYear(String header) throws NumberFormatException {
        int from;

        String params = header.substring(
                (from = header.indexOf("?") + 1),
                header.indexOf(" ", from)
        );

        int paramIndex = params.indexOf("=");
        if (paramIndex == -1 || !params.substring(0, paramIndex).equals("year")) {
            return -1;
        }

        int result;
        try {
            result = Integer.parseInt(params.substring(paramIndex + 1));
        } catch (NumberFormatException e) {
            return -2;
        }

        return result;
    }


    private String readHeader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;

        do {
            if ( (line = reader.readLine()) != null && !line.isEmpty()) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } while (line != null && !line.isEmpty());

        return builder.toString();
    }


    @Override
    public void run() {
        try {
            sendAnswer( generateAnswer( getYear( readHeader() ) ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
