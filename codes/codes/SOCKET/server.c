#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#pragma comment(lib, "ws2_32.lib")
#define PORT 8080
#define BUFFER_SIZE 1024
int main() {
    WSADATA wsa;
    SOCKET server_fd, new_socket;
    struct sockaddr_in address;
    int addrlen = sizeof(address);
    char buffer[BUFFER_SIZE] = {0};
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("Failed to initialize Winsock. Error Code: %d\n", WSAGetLastError());
        return 1;
    }
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("Socket creation failed. Error Code: %d\n", WSAGetLastError());
        WSACleanup();
        return 1;
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) == SOCKET_ERROR) {
        printf("Bind failed. Error Code: %d\n", WSAGetLastError());
        closesocket(server_fd);
        WSACleanup();
        return 1;
    }
    if (listen(server_fd, 3) == SOCKET_ERROR) {
        printf("Listen failed. Error Code: %d\n", WSAGetLastError());
        closesocket(server_fd);
        WSACleanup();
        return 1;
    }
    printf("Server listening on port %d...\n", PORT);
    if ((new_socket = accept(server_fd, (struct sockaddr *)&address, &addrlen)) == INVALID_SOCKET) {
        printf("Accept failed. Error Code: %d\n", WSAGetLastError());
        closesocket(server_fd);
        WSACleanup();
        return 1;
    }
    printf("Client connected! Start chatting.\n");
    while (1) {
        int bytes_received = recv(new_socket, buffer, BUFFER_SIZE, 0);
        if (bytes_received == SOCKET_ERROR || bytes_received == 0) {
            printf("Client disconnected.\n");
            break;
        }
        buffer[bytes_received] = '\0'; 
        printf("Client: %s\n", buffer);
        if (strcmp(buffer, "exit") == 0) {
            printf("Client ended the chat.\n");
            break;
        }
        printf("You: ");
        fgets(buffer, BUFFER_SIZE, stdin);
        buffer[strcspn(buffer, "\n")] = '\0'; 
        if (send(new_socket, buffer, strlen(buffer), 0) == SOCKET_ERROR) {
            printf("Send failed. Error Code: %d\n", WSAGetLastError());
            break;
        }
        if (strcmp(buffer, "exit") == 0) {
            printf("Exiting chat.\n");
            break;
        }
    }
    closesocket(new_socket);
    closesocket(server_fd);
    WSACleanup();

    return 0;
}
