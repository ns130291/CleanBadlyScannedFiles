CleanBadlyScannedFiles
======================

Cleans up B/W pictures

###before
![alt text](../blob/master/before.png)
###after
![alt text](../blob/master/after.png)

usage
-----
    java -jar CleanBadlyScannedFiles.jar [filename]

patterns
--------
Patterns which should be removed can be added as a two-dimensional array of `ints`.

Value  | Color
------------- | -------------
0  | White
1  | Black
-1 | Any
