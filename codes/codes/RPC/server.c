#include <stdio.h>
#include "fibonacci.h"

int fibonacci(int n) {
    if (n <= 0) return 0;
    else if (n == 1) return 1;
    else return fibonacci(n - 1) + fibonacci(n - 2);
}

int *fibonacci_1_svc(int *n, struct svc_req *rqstp) {
    static int result;
    result = fibonacci(*n);
    return &result;
}
