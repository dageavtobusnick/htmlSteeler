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
        System.out.println("Введите адрес сайта");
        String url=new Scanner(System.in).nextLine();
        try (Socket socket = new Socket(url, 80)) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write((String.format("GET / HTTP/1.1\r\nHost:%s\n" +
                    "Connection: keep-alive\r\n\r\n",url )).getBytes());
            outputStream.flush();
            String fileName = String.format("%s.html",url);
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
