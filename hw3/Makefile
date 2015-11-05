all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/PlayGame.java
	javadoc -d doc/ src/PlayGame.java
run:
	java -cp bin/ PlayGame
clean:
	rm -rf doc/
	rm -rf bin/*
