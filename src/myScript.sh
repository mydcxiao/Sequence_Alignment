#!/bin/bash
javac Basic.java
java Basic input1.txt
gtime -f "%e s \n%M KB" -a -o output.txt java Basic input1.txt