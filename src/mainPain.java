import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class mainPain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("www.google.com", 80)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(("GET / HTTP/1.1\r\nHost:www.google.com\n" +
                    "Connection: keep-alive\r\n\r\n").getBytes());
            outputStream.flush();
            String fileName = "wiki.html";
            try {
                Files.createFile(Path.of(fileName));
            } catch (FileAlreadyExistsException e) {

            }
            FileWriter fileWriter = new FileWriter(fileName);
            Scanner inputStream = new Scanner(socket.getInputStream());
            boolean stop = false;
            while (inputStream.hasNextLine()) {
                var read = inputStream.nextLine();
                if (read.contains("<!doctype html>"))
                    stop = true;
                if (stop) {
                    System.out.println(read);
                    fileWriter.write(read);
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
