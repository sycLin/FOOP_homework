all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/*.java -cp lib/
#	javadoc -package -d doc/ src/*.java
run:
	java -cp bin/:lib/ foop.POOCasino 10 5566 foop.PlayerB01902044 foop.PlayerB01902044 foop.PlayerB01902044 foop.PlayerB01902044
clean:
	rm -rf doc/
	rm -rf bin/
