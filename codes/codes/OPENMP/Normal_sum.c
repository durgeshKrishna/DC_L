
#include <stdio.h>
#include <time.h>

int main() {
    int n = 1000000000;         
//    int array[n];
    long long sum = 0;       
    // for (int i = 0; i < n; i++) {
    //     array[i] = i + 1;     
    // }
    clock_t start = clock();
    for (int i = 0; i < n; i++) {
        sum += i+1;
    }
    clock_t end = clock();
     printf("Sequential Sum: %lld\n", sum);
    printf("Execution time (Sequential): %f seconds\n", (double)(end - start) / CLOCKS_PER_SEC);
    return 0;
}