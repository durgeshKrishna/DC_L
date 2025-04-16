#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#pragma comment(lib, "ws2_32.lib")
#define BUFFER_SIZE 1024
int main() {
    WSADATA wsa;
    SOCKET sock;
    struct sockaddr_in server_addr;
    char buffer[BUFFER_SIZE] = {0};
    char ip[16];
    int port;
    printf("Enter server IP address: ");
    scanf("%15s", ip);
    printf("Enter server port number: ");
    scanf("%d", &port);
    getchar(); 
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0) {
        printf("Failed to initialize Winsock. Error Code: %d\n", WSAGetLastError());
        return 1;
    }
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
        printf("Socket creation failed. Error Code: %d\n", WSAGetLastError());
        WSACleanup();
        return 1;
    }
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    server_addr.sin_addr.s_addr = inet_addr(ip);
    if (server_addr.sin_addr.s_addr == INADDR_NONE) {
        printf("Invalid IP address.\n");
        closesocket(sock);
        WSACleanup();
        return 1;
    }
    if (connect(sock, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        printf("Connection failed. Error Code: %d\n", WSAGetLastError());
        closesocket(sock);
        WSACleanup();
        return 1;
    }
  printf("Connected to the server! Start chatting.\n");
    while (1) {
        printf("You: ");
        fgets(buffer, BUFFER_SIZE, stdin);
        buffer[strcspn(buffer, "\n")] = '\0'; 
        if (send(sock, buffer, strlen(buffer), 0) == SOCKET_ERROR) {
            printf("Send failed. Error Code: %d\n", WSAGetLastError());
            break;
        }
        if (strcmp(buffer, "exit") == 0) {
            printf("Exiting chat.\n");
            break;
        }
        int bytes_received = recv(sock, buffer, BUFFER_SIZE, 0);
        if (bytes_received == SOCKET_ERROR || bytes_received == 0) {
            printf("Server disconnected.\n");
            break;
        }
        buffer[bytes_received] = '\0'; 
        printf("Server: %s\n", buffer);
        if (strcmp(buffer, "exit") == 0) {
            printf("Server ended the chat.\n");
            break;
        }
        
    }
    closesocket(sock);
    WSACleanup();

    return 0;
}
