package any;
//sever side er jonno

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    File clientDirectory;  //it is on server side
    File clientDirectoryClientSide;
    private static final String END_OF_FILE_MARKER = "END_OF_FILE";
    private static final String END_OF_RESPONSE = "END_OF_RESPONSE";
    private static final int MAX_CHUNK_SIZE = 1024 * 100; // Maximum chunk size in bytes (e.g., 100 KB)
    private static final int MIN_CHUNK_SIZE = 1024; // Minimum chunk size in bytes (e.g., 1 KB)
    private static final int MAX_BUFFER_SIZE = 1024 * 1024;


    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;

    }

    @Override
    public void run() {
        try {
            // Initialize reader and writer
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Perform login process
            performLogin();
            boolean isConnected = true;
            System.out.println("loop e dhuki nai");
            System.out.println(clientSocket.getPort());

            //ekhane sob client request handle korbe
            while (isConnected) {
                //System.out.println("loop ");

                String request = reader.readLine();
                //System.out.println(request);
                if (request != null) {
                    if (request.equalsIgnoreCase("q")) {
                        isConnected = false;
                        server.removeClient(username);
                        System.out.println("hereeeeeee");
                        if (!server.searchInOfflineList(username))
                            server.disconnectedClients.put(username, clientSocket);
                        clientSocket.close();
                    }
                    if (request.equalsIgnoreCase("l")) {

                        System.out.println("ekhane aschi");
                        // Get a list of connected client names from the server
                        String connectedClients = server.getConnectedClients();
                        String offlineClients = server.getDisconnectedClients();
                        System.out.println("ekhane aschi");

                        // Send the list of connected clients to the client
                        writer.println("Connected clients (online):     " + connectedClients);
                        writer.println(" Other Connected clients (offline currently):     " + offlineClients);
                        writer.println(END_OF_RESPONSE);

                        //request = reader.readLine();

                    }
                    else if (request.equalsIgnoreCase("u")) {
                        handleFileUpload();
                    }

                    else if (request.equalsIgnoreCase("d")) {
                        handleFileDownload();
                    }
                    else if (request.equalsIgnoreCase("lookup")) {
                        sendFileList();
                    } else {
                        // Process other client requests

                        // writer.println("ei else e ");
                        // ..
                    }

                }

                writer.println("req null hoye gese ");
            }


            writer.println("loop er baire ");
//            // Handle client requests
//
//            // Close connections
//            reader.close();
//            writer.close();
//            clientSocket.close();
//
//            // Remove client from server's connected clients
//            server.removeClient(username);
//            System.out.println("Client " + username + " disconnected.");
        } catch (IOException e) {
            System.out.println("Connection error for client " + username + ": " + e.getMessage());


        }
    }

    private void sendFileList() throws IOException {
        writer.println("Listing uploaded files:");

        // Iterate over the uploadedFiles map
        for (Map.Entry <String, List <FileInfo>> entry : server.publiclyUploadedFiles.entrySet()) {
            String username = entry.getKey();

            List <FileInfo> files = entry.getValue();

            writer.println("User: " + username);
            //writer.println("User: " + this.username);

            if (username.equals(this.username)) {    //writer.println("same");
                List <FileInfo> associatedList = server.privatelyUploadedFiles.get(username);
                //System.out.println("size hocce "+server.privatelyUploadedFiles.size() +" list er size "+associatedList.size());

                if (associatedList != null) {
                    for (FileInfo file : associatedList) {
                        writer.println(file.fileName);
                    }

                }

            }

            if (files.isEmpty()) {
                writer.println("No public files uploaded.");
            } else {
                writer.println("Public Files:");
                for (FileInfo file : files) {
                    writer.println(file.fileName);
                }
            }

            //writer.println();  // Add an empty line between users
        }
        writer.println("List sent successfully ");
        writer.println(END_OF_RESPONSE);

    }

    private void performLogin() throws IOException {
        writer.println("Please enter your username:");
        String loginRequest = reader.readLine();
        if (server.isUsernameTaken(loginRequest, clientSocket)) {
            writer.println("Username already in use. Connection terminated.");
            clientSocket.close();
            return;
        }
        username = loginRequest;
        server.addClient(username, clientSocket);

        // Create directory for the client if it doesn't exist
        clientDirectory = new File(server.baseDirectory, username);
        if (!clientDirectory.exists()) {
            if (clientDirectory.mkdir()) {
                System.out.println("Directory created for client " + username);
            } else {
                System.out.println("Failed to create directory for client " + username);
            }
        }

        clientDirectoryClientSide = new File(server.baseDirectoryClientSide, username);
        if (!clientDirectoryClientSide.exists()) {
            if (clientDirectoryClientSide.mkdir()) {
                System.out.println("Directory created for client on client side" + username);
            } else {
                System.out.println("Failed to create directory for client on client side" + username);
            }
        }

        writer.println("Welcome, " + username + "! You are now connected to the server.");
        System.out.println("Client " + username + " connected.");  //server side e
    }

    //file receive er jonno ja client side theke pathacche


    private String generateFileID() {
        return UUID.randomUUID().toString();
    }



    private void handleFileDownload() throws IOException {
        String filename = reader.readLine();
        File file = new File(filename);

        if (file.exists()) {
            writer.println("File found");

            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file))) {
                OutputStream outputStream = clientSocket.getOutputStream();
                byte[] buffer = new byte[4096]; // Adjust the buffer size as per your requirements
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            writer.println(END_OF_FILE_MARKER);
            writer.flush();
        } else {
            writer.println("File not found");
        }
    }

//    private void handleFileDownload() throws IOException {
//        String filename = reader.readLine();
//        File file = new File(filename);
//
//        if (file.exists()) {
//            writer.println("File found");
//
//            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file))) {
//                byte[] buffer = new byte[4096]; // Adjust the buffer size as per your requirements
//                int bytesRead;
//                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                    writer.write(String.valueOf(buffer), 0, bytesRead);
//                }
//            }
//
//            writer.println(END_OF_FILE_MARKER);
//            writer.flush();
//        } else {
//            writer.println("File not found");
//        }
//    }







    private void handleFileUpload() throws IOException {
        String filename = reader.readLine();
        String visibility = reader.readLine();
        File file = new File(clientDirectory, filename);

        // Check if total file size exceeds maximum buffer size
        long totalFileSize = file.length();
        long totalBufferedSize = server.current_buffer_size;
        if (totalFileSize + totalBufferedSize > MAX_BUFFER_SIZE) {
            writer.println("File upload exceeds maximum buffer size. Please try again with a smaller file.");
            return;
        }

        // Randomly generate chunkSize within the specified range
        int chunkSize = (int) (MIN_CHUNK_SIZE + Math.random() * (MAX_CHUNK_SIZE - MIN_CHUNK_SIZE));

        // Generate a unique fileID
        String fileID = generateFileID();

        // Add fileID and other necessary information to the server's data structures


        // Send confirmation message to the client with chunkSize and fileID
        writer.println("OK");
        writer.println(chunkSize);
        writer.println(fileID);
        server.current_buffer_size+=totalFileSize;

        int totalChunks = Integer.parseInt(reader.readLine());


//        int chunkSentByClient=0;
//        String res;
//        while (true)
//        {
//            //System.out.println(reader.readLine());
//            res=reader.readLine();
//            if(res.equals("END_OF_CHUNK")) break;
//           // chunkSentByClient= Integer.parseInt(reader.readLine());
//            writer.println("Received number "+res+" chunk");
//
//        }





        try (BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.equals(END_OF_FILE_MARKER)) {
                byte[] buffer = line.getBytes();
                fileOutputStream.write(buffer);
            }

            fileOutputStream.flush();  // Flush the file output stream( nao dite pari)
        }




            if (visibility.equalsIgnoreCase("public")) {

                List <FileInfo> list = server.publiclyUploadedFiles.getOrDefault(username, new ArrayList <>());
                FileInfo fn = new FileInfo();
                fn.fileName = filename;
                fn.chunksize = chunkSize;
                fn.fileId = fileID;
                list.add(fn);
                server.publiclyUploadedFiles.put(username, list);


            } else {
                List <FileInfo> list = server.privatelyUploadedFiles.getOrDefault(username, new ArrayList <>());
                FileInfo fn = new FileInfo();
                fn.fileName = filename;
                fn.chunksize = chunkSize;
                fn.fileId = fileID;
                list.add(fn);
                server.privatelyUploadedFiles.put(username, list);
                System.out.println("private e rakhsi " + server.privatelyUploadedFiles.size());
            }

            writer.println("File uploaded successfully.");
            // writer.println(END_OF_RESPONSE);

    }
}
