package any;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 56565;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private static final String END_OF_FILE_MARKER = "END_OF_FILE";
    private static final String END_OF_RESPONSE = "END_OF_RESPONSE";

    private static final int MAX_CHUNK_SIZE = 1024 * 100; // Maximum chunk size in bytes (e.g., 100 KB)
    private static final int MIN_CHUNK_SIZE = 1024; // Minimum chunk size in bytes (e.g., 1 KB)
    static final long MAX_CAPACITY = 1024 * 1024*1024*1024;


    public void start(String host, int port)
    {
        try{
          socket = new Socket(SERVER_IP, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            // Read and display the server's welcome message
            String serverMessage = reader.readLine();
            System.out.println(serverMessage);

            // Get the client's username from the user
            String username = userInputReader.readLine();

            // Send the username to the server
            writer.println(username);

            // Receive and display the server's response
            serverMessage = reader.readLine();
            System.out.println(serverMessage);

            // Perform file operations based on user commands
            String command;


//            while (true) {
//                System.out.print("> ");
//                command = userInputReader.readLine();
//                writer.println(command);
//
//                if (command.equalsIgnoreCase("quit")) {
//                    break;
//                }
//
//                // Receive and display the server's response
//                serverMessage = reader.readLine();
//                System.out.println(serverMessage);
//            }

            while (true)

            {

                System.out.print("Enter a command: ");
                 command = userInputReader.readLine();


                if (command.equalsIgnoreCase("q"))
                {
                    writer.println(command); // clienthandler handle korbe
                    System.out.println("Your connection is terminated");
                    break;
                }

                else if (command.equalsIgnoreCase("u"))
                {
                    handleFileUploadFromConsole();
                }
                else if (command.equalsIgnoreCase("lookup"))
                {
                    handleFileLookup();

                }
                else if (command.equalsIgnoreCase("d"))
                {
                    handleFileDownloadFromConsole();

                }

                else
                {
                    writer.println(command);
                    writer.flush();
                    String response;
                    System.out.println("ki?");
                    response=reader.readLine();
                    while (!(response.equals(END_OF_RESPONSE)) )
                    {
                        System.out.println(response);
                        response=reader.readLine();


                    }

                }
            }


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    private void handleFileLookup() throws IOException {
        writer.println("lookup");
        writer.flush();

        // Receive and display the list of files
        String response;
        response=reader.readLine();
        while (!(response.equals(END_OF_RESPONSE)) )
        {
            System.out.println(response);
            response=reader.readLine();


        }

//        writer.flush();
//        String response = reader.readLine(); //server er response pawar jonno
//        System.out.println("Server response: " + response);

//        while ((response = reader.readLine()) != null) {
//            if (response.equals(END_OF_RESPONSE)) {
//                break;
//            }
//            System.out.println(response);
//        }



    }
    private void handleFileUploadFromConsole() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the file path: ");
        String filePath = consoleReader.readLine();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        System.out.println("Specify file visibility (public/private): ");
        String visibility = consoleReader.readLine();

        String filename = file.getName();
        int fileSize = Math.toIntExact(file.length());
        writer.println("u");
        writer.println(filename); //to send commands and data to server
        writer.println(visibility);     // public or private
        writer.println(fileSize);



        String serverResponse = reader.readLine(); // Read server response
        if (serverResponse.equals("OK"))

        {

            int chunkSIZE= Integer.parseInt(reader.readLine());
            System.out.println("Chunk size given by server is "+chunkSIZE);
            String fileId=reader.readLine();
            System.out.println("File id is "+fileId);
            System.out.println("file size is "+fileSize);
            int totalChunks = (int) Math.ceil((fileSize) / chunkSIZE);
            writer.println(totalChunks); // Send total number of chunks to server

            long remainingSize = fileSize;
            int chunk;


//            for (int chunkIndex = 1; chunkIndex <=totalChunks; chunkIndex++)
//            {   String h= String.valueOf(chunkIndex);
//               writer.println(h);
//               String acknowledgement=reader.readLine();
//               writer.println(acknowledgement);
//            }
//
//            writer.println("END_OF_CHUNK");

            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[fileSize];  // Adjust the buffer size as per your requirements
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    writer.println(new String(buffer, 0, bytesRead));
                }

                writer.println(END_OF_FILE_MARKER);
            }
            writer.flush();

            String finalResponse = reader.readLine(); // Read final server response
            System.out.println("Server response: " + finalResponse);
        } else {
            System.out.println("Server rejected the file upload.");
        }
        System.out.println(socket.getLocalPort());
    }

//

    private void handleFileDownloadFromConsole() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Whose file do you want to download?");
        String clientName = consoleReader.readLine();
        System.out.println("Which file?");
        String filename = consoleReader.readLine();
        String path = "D:\\ServerFiles\\" + clientName + "\\" + filename;
        String savePath = "D:\\ClientFiles\\" + clientName + "\\" + filename;

        writer.println("d"); // Send the download command to the server
        writer.println(path); // Send the filename to download

        String response = reader.readLine(); // Read the server's response
        if (response.equals("File found")) {
            System.out.println("Downloading file...");

            try (InputStream inputStream = socket.getInputStream();
                 FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

                byte[] buffer = new byte[4096]; // Adjust the buffer size as per your requirements
                int bytesRead;
                boolean endOfFile = false;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    String receivedData = new String(buffer, 0, bytesRead);
                    if (receivedData.contains(END_OF_FILE_MARKER)) {
                        endOfFile = true;
                        break;
                    }
                }

                if (endOfFile) {
                    System.out.println("File downloaded successfully.");
                    // Continue with other operations using the same socket if needed
                } else {
                    System.out.println("Error: Unexpected end of file marker.");
                }
            }
        } else {
            System.out.println("File not found on the server.");
        }
        System.out.println(socket.getLocalPort());

    }


//    private void handleFileDownloadFromConsole() throws IOException {
//        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Whose file you want to download?");
//        String clientName = consoleReader.readLine();
//        System.out.println("Which file?");
//        String filename = consoleReader.readLine();
//        String path = "D:\\ServerFiles\\" + clientName + "\\" + filename;
//
//        System.out.println(path);
//
//        writer.println("d"); // Send the download command to the server
//        writer.println(path); // Send the filename to download
//
//        String response = reader.readLine(); // Read the server's response
//        if (response.equals("File found")) {
//            System.out.println("Downloading file...");
//
//            try (InputStream inputStream = socket.getInputStream();
//                 FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
//
//                byte[] buffer = new byte[4096]; // Adjust the buffer size as per your requirements
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    fileOutputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            System.out.println("File downloaded successfully.");
//        } else {
//            System.out.println("File not found on the server.");
//        }
//    }




    public static void main(String[] args) {

        String host = "localhost";
        int port = 56565;

        Client client = new Client();
        client.start(host, port);

       
    }
}
