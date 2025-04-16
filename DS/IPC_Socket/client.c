#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<winsock2.h>
#pragma comment(lib,"ws2_32.lib")
void main(){
    WSADATA wsa;
    SOCKET clientSocket;
    struct sockaddr_in server_addr;
    WSAStartup(MAKEWORD(2,2),&wsa);
    clientSocket = socket(AF_INET,SOCK_STREAM,0);
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(8080);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    connect(clientSocket,(struct sockaddr*)&server_addr,sizeof(server_addr));
    printf("Server Connected!..\n");
    char buffer[1024];
    int byteReceived;
    while(1){
        printf("Client: ");
        fgets(buffer,sizeof(buffer),stdin);
        buffer[strcspn(buffer, "\n")] = '\0';
        send(clientSocket,buffer,strlen(buffer)+1,0);
        if(!strcmp(buffer,"exit")){
            printf("Disconnected!!..\n");
            break;
        }
        byteReceived = recv(clientSocket,buffer,sizeof(buffer),0);
        buffer[byteReceived] = '\0';
        printf("Msg from Server: %s",buffer);
    }
    closesocket(clientSocket);
    WSACleanup();
}
