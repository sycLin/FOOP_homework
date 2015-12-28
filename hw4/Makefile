all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/*.java -cp lib/
#	javadoc -package -d doc/ src/*.java
run:
	java -cp bin/:lib/ foop.POOCasino hi hello how are you
clean:
	rm -rf doc/
	rm -rf bin/
