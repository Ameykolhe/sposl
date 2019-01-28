	LOAD	a
	STORE	b
	MACRO	M1	&x,&y,&z
	LOAD	p
	LOAD	&x
	&z	&y
	LOAD	&y
	STORE	&x
	M2	&x,&y
	MEND
	MACRO	M2	&t,&u
	LOAD	p
	STORE	&t
	ADD	&u
	SUB	&t
	STORE	&u
	MEND
	M1	a,b,ADD
	END
