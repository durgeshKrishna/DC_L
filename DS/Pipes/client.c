#include<windows.h>
#include<stdio.h>
#define PIPE_NAME "\\\\.\\pipe\\MyPipe"
void main(){
    HANDLE pipe;
    char buffer[1024];
    DWORD byteRead,byteWrite;
    pipe = CreateFileA(PIPE_NAME,GENERIC_READ | GENERIC_WRITE, 0 ,NULL, OPEN_EXISTING, 0,NULL);
    printf("Server Connected..\n");
    while (1){
        printf("Client: ");
        fgets(buffer,sizeof(buffer),stdin);
        buffer[strcspn(buffer,"\n")] = '\0';
        WriteFile(pipe,buffer,strlen(buffer)+1,&byteWrite,NULL);
        if(!strcmp(buffer,"exit")){
            printf("Disconnected!..");
            break;
        }
        ReadFile(pipe,buffer,sizeof(buffer),&byteRead,NULL);
        buffer[byteRead] = '\0';
        printf("From Server: %s\n",buffer);
    }
    CloseHandle(pipe);
}