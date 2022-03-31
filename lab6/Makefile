RM = rm -f
lab6: 
	javac lab6.java
clean: 
	$(RM) *.class

test: testing1 testing2

testing1: 
	java lab6 mem_stream.1 > output1 
	diff -w -B output1 mem_stream1.out.txt

testing2: 
	java lab6 mem_stream.2 > output2 
	diff -w -B output2 mem_stream2.out.txt
