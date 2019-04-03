#include<mycalc.h>
#include<stdio.h>

int main(){
	int x,y;
	scanf("%d %d",&x,&y);
    printf("Enter X & Y : ");
	printf("\nSum  of X & Y is %d",add(x,y));
	printf("\nDiff of X & Y is %d\n",sub(x,y));
	return 0;
}
