all:
	if ! [ -d "bin" ]; then mkdir bin; fi
	javac -d bin/ src/*.java -cp lib/
run:
	java -cp bin/:lib/ foop.POOCasino 2000 5566 foop.PlayerB01902044 foop.PlayerB01902032 foop.PlayerB01902044 foop.PlayerB01902032 2>/dev/null
run-dev:
	java -cp bin/:lib/ foop.POOCasino 2000 5566 foop.PlayerB01902044 foop.PlayerB01902032 foop.PlayerB01902044 foop.PlayerB01902032
doc-gen:
	javadoc -private -d doc/ src/*.java
clean:
	rm -rf doc/
	rm -rf bin/
