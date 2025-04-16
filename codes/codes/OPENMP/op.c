#include <stdio.h>
#include <omp.h>

int main() {
    int n = 1000000;         
    int array[n];
    long long sum = 0;        
    // for (int i = 0; i < n; i++) {
    //     array[i] = i + 1;     
    // }
    double start_time = omp_get_wtime();
    #pragma omp parallel for reduction(+:sum)
    for (int i = 0; i < n; i++) {
        sum += i+1;
    }
    double end_time = omp_get_wtime();
    printf("Parallel Sum: %lld\n", sum);
    printf("Execution time (Parallel): %f seconds\n", end_time - start_time);
    return 0;
}
