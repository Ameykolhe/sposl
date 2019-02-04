%{
	/* Program to count words, characters and lines */
	unsigned charCount=0, wordCount=0, lineCount=0;
%}
word	[^ \t\n]+
eol	\n
%%
{word}	{wordCount++; charCount+=yyleng;}
{eol}	{lineCount++;}
.	{charCount++;}
%%
main()	
{	
	extern FILE *yyin;
	yyin = fopen("input.txt", "r");
	yylex();
	printf("%d %d %d\n", lineCount, wordCount, charCount);
}
