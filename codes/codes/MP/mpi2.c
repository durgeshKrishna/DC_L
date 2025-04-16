#include <mpi.h>
#include <stdio.h>

int main(int argc, char** argv) {
    int rank, size, N = 100; 
    int local_sum = 0, global_sum = 0;
    double start_time, end_time;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    int chunk_size = N / size;     
    int start = rank * chunk_size + 1; 
    int end = (rank + 1) * chunk_size; 
    if (rank == size - 1) {
        end = N;
    }
    if (rank == 0) {
        start_time = MPI_Wtime();
    }
    for (int i = start; i <= end; i++) {
        local_sum += i;
    }
    printf("Process %d calculated local sum = %d (range %d to %d)\n", rank, local_sum, start, end);
    MPI_Reduce(&local_sum, &global_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        printf("The total sum of the first %d natural numbers is %d\n", N, global_sum);
    }
    if (rank == 0) {
        end_time = MPI_Wtime();
        printf("Total execution time: %f seconds\n", end_time - start_time);
    }

    MPI_Finalize();
    return 0;
}
