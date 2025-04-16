#include<stdio.h>
#include<stdlib.h>
#include<omp.h>

typedef struct matrix{
    int row;
    int col;
    int **array;
}matrix;
void print_mat(matrix* mat){
    for(int i=0;i<mat->row;i++){
        for(int j=0;j<mat->col;j++){
            printf("%d ",mat->array[i][j]);
        }
        printf("\n");
    }
}
matrix* matrix_mul(matrix* mat1,matrix* mat2){
    matrix* mat3 = (matrix*)malloc(sizeof(matrix));
    mat3->row = mat1->row;
    mat3->col = mat2->col;
    mat3->array = (int**)malloc(mat3->row*sizeof(int*));

    omp_set_num_threads(4);
    #pragma omp parallel shared(mat1,mat2,mat3)
    {
        #pragma for schedule(dynamic)
        for(int i=0;i<mat3->row;i++){
            int tid = omp_get_thread_num();
            mat3->array[i] = (int*)malloc(mat3->col*sizeof(int));
            for(int j=0;j<mat3->col;j++){
                //printf("Thread ID %d is working on %d row of matrix 1 and %d column of matrix 2\n",tid, i, j);
                int temp = 0;
                for(int k=0;k<mat2->row;k++){
                    temp += mat1->array[i][k]*mat2->array[k][j];
                }
                mat3->array[i][j] = temp;
            }
        }
    }
    return mat3;
}


void main(){
    srand(0); 
    matrix *mat1 = (matrix*)malloc(sizeof(matrix));
    matrix *mat2 = (matrix*)malloc(sizeof(matrix));
    printf("Size of matrix1: ");
    scanf("%d %d",&mat1->row,&mat1->col);
    printf("Size of matrix2: ");
    scanf("%d %d",&mat2->row,&mat2->col);
    mat1->array = (int**)malloc(mat1->row*sizeof(int *));
    mat2->array = (int**)malloc(mat2->row*sizeof(int *));
    for(int i=0;i<mat1->row;i++){
        mat1->array[i] = (int*)malloc(mat1->col*sizeof(int));
        for(int j=0;j<mat1->col;j++){
            mat1->array[i][j] = (rand()%10) + 1;
        }
    }
    for(int i=0;i<mat2->row;i++){
        mat2->array[i] = (int*)malloc(mat2->col*sizeof(int));
        for(int j=0;j<mat2->col;j++){
            mat2->array[i][j] = (rand()%10) + 1;
        }
    }
    printf("Matrix1: \n"); print_mat(mat1);
    printf("Matrix2: \n"); print_mat(mat2); 
    double t1 = omp_get_wtime();
    matrix* mat3 = matrix_mul(mat1,mat2);
    double t2 = omp_get_wtime();
    printf("mat1*mat2== \n"); print_mat(mat3);
    printf("\nFor parallel-tasking : %lf\n",t2-t1);
}
//ubuntu...
//gcc -fopenmp matrix_mul.c -o a -lpthread
//./a.out