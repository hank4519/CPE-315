RM = rm -f 
lab4: 
	javac lab4.java
clean: 
	$(RM) *.class
	
testing1:
	java lab4 test1 script1 > myoutput1 
	diff -w -B myoutput1 output1
testing2:	 	
	java lab4 test2 script2 > myoutput2 
	diff -w -B myoutput2 output2
testing3:
	java lab4 test3 script3 > myoutput3 
	diff -w -B myoutput3 output3
testing4:	
	java lab4 test4 script4 > myoutput4 
	diff -w -B myoutput4 output4 	

test: testing1 testing2 testing3 testing4

