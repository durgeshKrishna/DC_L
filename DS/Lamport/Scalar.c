#include<stdio.h>
#include<stdlib.h>
void print_mat(int e,int p, int mat[e][p]){
    printf("%-2c",' ');
    for(int i=0;i<p;i++) printf(" P%-2d ",i);
    printf("\n");
    for(int i=0;i<e;i++){
        printf("e%d ",i);
        for(int j=0;j<p;j++){
            printf("%-3d ",mat[i][j]);
        }
        printf("\n");
    }
}
void main(){
    int e,p,sp,se,rp,re;
    printf("No.of Events: ");
    scanf("%d",&e);
    printf("No.of Process: ");
    scanf("%d",&p);
    int mat[e][p];
    int tick[p];
    for(int i=0;i<p;i++){
        printf("Tick Value of Process P%d: ",i);
        scanf("%d",&tick[i]);
        mat[0][i] = tick[i];
    }
    for(int i=1;i<e;i++){
        for(int j=0;j<p;j++){
            mat[i][j] = mat[i-1][j] + tick[j];
        }
    }
    print_mat(e,p,mat);
    int n;
    printf("No.of messages: ");
    scanf("%d",&n);
    for(int k=0;k<n;k++){
        printf("Enter (senderProcess,senderEvent,ReceiverProcess,ReceiverEvent): ");
        scanf("%d %d %d %d",&sp,&se,&rp,&re);
        if(mat[se][sp] < mat[re][rp]){
            printf("Happen-before -- preserved..\n");
        }else{
            mat[re][rp] = mat[se][sp] + 1;
            for(int i=re+1;i<e;i++){
                mat[i][rp] = mat[i-1][rp] + tick[rp]; 
            }
            print_mat(e,p,mat);
        }
    }
    printf("\nFinal : *** \n");
    print_mat(e,p,mat);
}