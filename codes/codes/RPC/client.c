#include <stdio.h>
#include <stdlib.h>
#include "fibonacci.h"

int main(int argc, char *argv[]) {
    CLIENT *clnt;
    int *result, num;

    if (argc != 3) {
        printf("Usage: %s <server_host> <number>\n", argv[0]);
        exit(1);
    }

    num = atoi(argv[2]);
    clnt = clnt_create(argv[1], FIBONACCI_PROG, FIBONACCI_VERS, "udp");

    if (clnt == NULL) {
        clnt_pcreateerror(argv[1]);
        exit(1);
    }

    result = fibonacci_1(&num, clnt);

    if (result == NULL) {
        clnt_perror(clnt, "RPC call failed");
        exit(1);
    }

    printf("Fibonacci(%d) = %d\n", num, *result);
    clnt_destroy(clnt);
    return 0;
}
