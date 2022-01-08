# Coordinate Match Generator

This repository contains two different implementations that were used
as a submission for a coding challenge.

Context:
A program is required to process 2 files of different formats (one CSV, 
the other JSON) as input. The files contain coordinate data (WGS 84) corresponding
to flying objects on the earth. Each file is meant to represent incoming data from
a sensor. Ideally, both datasets would completely agree and contain, if
not in the same order, the same (object ID, coordinates) pairings. However,
the incoming data is subject to error, and this agreement cannot be assumed - 
one sensor may detect objects that the other does not. Furthermore, the two sensors
may have assigned a different ID to an object during operation without synchronizing.
Therefore, ID alone cannot be used to determine if coordinates from the two sensors 
are referring to the same object.

Problem:
Given,
    1) Maximum disagreement between the sensors is 100 metres. If a set of 
    coordinates in the CSV file is within 100 metres of a set in the JSON
    file, these coordinates belong to the same object.
    2) All objects are more than 100 metres apart at any time.

Process the coordinates from the two files to determine which object IDs from 
the two datasets correspond to the same object, and which IDs are unique to either
dataset (objects that were missed by the other sensor). Generate the output as a .txt
file with the following format:

<CSV_SENSOR_ID>:<JSON_SENSOR_ID>

If a flying object was only picked up by one of the sensors, the other sensor's ID
should be reported as a -1.

IMPLEMENTATION COMMENTS

Firstly, I mainly focused on implementing a build that could process data 
quickly in C. The python folder contains my initial efforts to explore the
problem and make error comparisons between my algorithms of choice and established
geolocation libraries, such as Geopy. If there is interest in my approach to data
analysis with the use of numpy, matplotlib and pandas, see the 'python' folder. 
Otherwise, one may focus on the 'c' folder.

I have included a Makefile, making compilation easy if you have installed the
GNU make utility in your system.

If you do not have GNU make, these commands will create an equivalent build:

gcc -Wall -g -Iheader -c src/smallAngleDistance.c -o obj/smallAngleDistance.o  
gcc -Wall -g -Iheader -c src/outputGenerator.c -o obj/outputGenerator.o  
gcc -Wall -g -Iheader -c src/main.c -o obj/main.o  
gcc -Wall -g -Iheader -c src/coordinatesJSONparser.c -o obj/coordinatesJSONparser.o  
gcc -Wall -g -Iheader -c src/linkedListQueue.c -o obj/linkedListQueue.o  
gcc -Wall -g -Iheader -c src/coordinatesCSVparser.c -o obj/coordinatesCSVparser.o  
gcc -Wall -g -Iheader -c src/linkedList.c -o obj/linkedList.o  
gcc -Wall -g -Iheader  obj/smallAngleDistance.o  obj/outputGenerator.o  obj/main.o  obj/coordinatesJSONparser.o  obj/linkedListQueue.o  obj/coordinatesCSVparser.o  obj/linkedList.o -o bin/main  

You should be able to copy and paste those lines onto the command line and press enter
to complete the build.

As for running the project, I have used command-line arguments to process
coordinate files. From the same directory in which you compiled, enter the 
following sequence:

bin/main CSVfilename.csv JSONfilename.json

for example:

bin/main input.csv input.json

The CSV must be entered before the JSON as I depend on that
sequence to apply the correct parser.

That should be it! Solution files may be found in the 'c/output' directory. They
are generated from the most recent project build.

If you are interested in checking out my Python work, it is fairly analogous
to my approach in C, but I also took the time to explore the data. I set up some
scripts to generate output with various methods, ranging from computationally
expensive to suitably efficient to completely inaccurate. I also experimented
with visualizing the data and plotting it on a 3D unit sphere. I made heavy
use of pandas DataFrame objects to play around with the problems. Finally, I 
did use the Geopy library to generate a 'definitive' reference against which I
compared my results from both the python and C code.

Thank you for reading!