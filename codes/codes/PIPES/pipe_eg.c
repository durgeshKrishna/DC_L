#include <windows.h>
#include <stdio.h>
#include <string.h>

int main() {
    HANDLE readHandleClientToServer, writeHandleClientToServer;
    HANDLE readHandleServerToClient, writeHandleServerToClient;
    SECURITY_ATTRIBUTES sa = {sizeof(SECURITY_ATTRIBUTES), NULL, TRUE};
    char buffer[100];
    DWORD bytesRead, bytesWritten;
    if (!CreatePipe(&readHandleClientToServer, &writeHandleClientToServer, &sa, 0)) {
        fprintf(stderr, "Pipe creation failed (Client to Server)\n");
        return 1;
    }
    if (!CreatePipe(&readHandleServerToClient, &writeHandleServerToClient, &sa, 0)) {
        fprintf(stderr, "Pipe creation failed (Server to Client)\n");
        return 1;
    }
    printf("Enter message to send to server (type 'exit' to quit): ");
    while (fgets(buffer, sizeof(buffer), stdin)) {
        buffer[strcspn(buffer, "\n")] = '\0';
        if (!WriteFile(writeHandleClientToServer, buffer, strlen(buffer) + 1, &bytesWritten, NULL)) {
            fprintf(stderr, "WriteFile failed (Client -> Server)\n");
            return 1;
        }
        if (strcmp(buffer, "exit") == 0) {
            break;
        }
        if (ReadFile(readHandleClientToServer, buffer, sizeof(buffer), &bytesRead, NULL)) {
            buffer[bytesRead] = '\0';
            printf("Server received: %s\n", buffer);
            printf("Enter message to send back to client: ");
            fgets(buffer, sizeof(buffer), stdin);
            buffer[strcspn(buffer, "\n")] = '\0';

            if (!WriteFile(writeHandleServerToClient, buffer, strlen(buffer) + 1, &bytesWritten, NULL)) {
                fprintf(stderr, "WriteFile failed (Server -> Client)\n");
                return 1;
            }
        } else {
            fprintf(stderr, "ReadFile failed (Client -> Server)\n");
            break;
        }
        if (ReadFile(readHandleServerToClient, buffer, sizeof(buffer), &bytesRead, NULL)) {
            buffer[bytesRead] = '\0';
            printf("Received from server: %s\n", buffer);
        } else {
            fprintf(stderr, "ReadFile failed (Server -> Client)\n");
            break;
        }
        printf("Enter message to send to server: ");
    }
    CloseHandle(readHandleClientToServer);
    CloseHandle(writeHandleClientToServer);
    CloseHandle(readHandleServerToClient);
    CloseHandle(writeHandleServerToClient);

    return 0;
}
