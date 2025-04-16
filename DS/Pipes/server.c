#include<windows.h>
#include<stdio.h>
#define PIPE_NAME "\\\\.\\pipe\\MyPipe"
void main(){
    char buffer[1024];
    HANDLE pipe;
    DWORD byteRead,byteWrite;
    pipe = CreateNamedPipeA(
        PIPE_NAME, PIPE_ACCESS_DUPLEX,PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE | PIPE_WAIT,
        1,1024,1024,0,NULL);
    printf("Waiting for Client to connect..");
    ConnectNamedPipe(pipe,NULL);
    printf("\nClient Connected..\n");
    while(1){
        ReadFile(pipe,buffer,sizeof(buffer),&byteRead,NULL);
        buffer[byteRead] = '\0';
        if(!strcmp(buffer,"exit")){
            printf("Disconnected!!\n");
            break;
        }
        printf("Client: %s\n",buffer);
        printf("Server message to Client: ");
        fgets(buffer,sizeof(buffer),stdin);
        buffer[strcspn(buffer,"\n")] = '\0';
        WriteFile(pipe, buffer,strlen(buffer)+1,&byteWrite,NULL);
    }
    CloseHandle(pipe);
}