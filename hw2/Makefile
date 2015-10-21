all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/JoB.java src/RandomIndex.java
	javadoc -d doc/ src/JoB.java
run:
	java -cp bin/ JoB
clean:
	rm -rf src/*.class
	rm -rf doc/
	rm -rf bin/*
