all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/PlayGame.java src/Var1.java
	javadoc -package -d doc/ src/*.java
run:
	java -cp bin/ PlayGame
clean:
	rm -rf doc/
	rm -rf bin/*
