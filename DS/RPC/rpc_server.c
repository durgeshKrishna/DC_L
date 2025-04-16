#include<stdio.h>
#include<tirpc/rpc/rpc.h>
#include"sumofdigit.h"

int sum_of_digits(int n){
//    while(n > 9){
//        int sum = 0;
//        while(n!=0){
//            int rem = n%10;
//            sum += rem;
//            n /= 10;
//        }
//        n = sum;
//    }
    return n*10000;
}
int result;
//rpc - implementation:
int *sumofdigit_1_svc(int *num, struct svc_req *req) {
   // static int result;
    result = sum_of_digits(*num); 
    return &result;
}

//gcc -o server rpc_server.c sumofdigit_svc.c -I/usr/include/tirpc -ltirpc
//./server
