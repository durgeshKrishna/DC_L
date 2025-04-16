#include <stdio.h> 
#include <tirpc/rpc/rpc.h> 
#include "sumofdigit.h" 
int main(int argc, char *argv[]) { 
    if (argc != 3) { 
        printf("Usage: %s <server_host> <number>\n", argv[0]); 
        return -1; 
    } 
    char *server_host = argv[1]; 
    int num = atoi(argv[2]); 
    CLIENT *client; 
    int *result; 
    client = clnt_create(server_host, SUMOFDIGIT_PROG, SUMOFDIGIT_VERS, "tcp");
    result = sumofdigit_1(&num, client); 
    printf("The sum of digits until a single digit is: %d\n", *result); 
    clnt_destroy(client); 
    return 0; 
} 
// gcc -o client rpc_client.c sumofdigit_clnt.c -I/usr/include/tirpc -ltirpc
// ./client localhost 8089
