#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<winsock2.h>
#pragma comment(lib,"ws2_32.lib");
void main(){
    WSADATA wsa;
    SOCKET serverSocket,clientSocket;
    struct sockaddr_in server_addr,client_addr;
    WSAStartup(MAKEWORD(2,2),&wsa);
    serverSocket = socket(AF_INET,SOCK_STREAM,0);
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(8080);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    bind(serverSocket,(struct sockaddr*)&server_addr,sizeof(server_addr));
    listen(serverSocket,2);
    printf("Server Listening in port 8080..\n");
    int clientSize = sizeof(client_addr);
    clientSocket = accept(serverSocket,(struct sockaddr*)&client_addr,&clientSize);
    printf("Client Connected..\n");
    char buffer[1024];
    int byteReceived;
    while(1){
        byteReceived = recv(clientSocket,buffer,sizeof(buffer),0);
        buffer[byteReceived] = '\0';
        if(!strcmp(buffer,"exit")){
            printf("Disconnected...\n");
            break;
        }
        printf("Msg from Client: %s\n",buffer);
        printf("Server : ");
        fgets(buffer,sizeof(buffer),stdin);
        send(clientSocket,buffer,strlen(buffer)+1,0);
    }
    closesocket(serverSocket);
    closesocket(clientSocket);
    WSACleanup();
}
//gcc server.c -o s -lws2_32
