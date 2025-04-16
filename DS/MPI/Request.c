#include<mpi.h>
#include<stdio.h>
#include<stdlib.h>

int isduplicate(int *arr,int size,int id){
    for(int i=0;i<size;i++) if(arr[i]==id) return 1;
    return 0;
}

void main(int args,char **argv){
    int rank,size;
    MPI_Init(&args,&argv);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);
    MPI_Comm_size(MPI_COMM_WORLD,&size);
    srand(0);
    int req_per_worker = 1000/(size-1);
    int received_req[req_per_worker];
    if(rank == 0){
        int req[1000];
        for(int i=0;i<1000;i++) req[i] = rand()%100000;
        MPI_Scatter(req,req_per_worker,MPI_INT,received_req,req_per_worker,MPI_INT,0,MPI_COMM_WORLD);
    }else{
        MPI_Scatter(NULL,0,MPI_INT,received_req,req_per_worker,MPI_INT,0,MPI_COMM_WORLD);
        //check duplicates:
        int processed_id[req_per_worker];
        int count=0;
        for(int i=0;i<req_per_worker;i++){
            int id = received_req[i];
            if(!isduplicate(processed_id,count,id)){
                processed_id[count++] = id;
                printf("Worker %d - Serviced the req. %d\n",rank,id);
            }
        }
        MPI_Gather(&count,1,MPI_INT,NULL,1,MPI_INT,0,MPI_COMM_WORLD);
    }
    MPI_Finalize();
}

//mpicc Request.c
//mpirun -np 4 ./a.out