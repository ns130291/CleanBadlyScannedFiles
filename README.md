CleanBadlyScannedFiles
======================

Cleans up B/W pictures

###before
![before](../master/before.png)
###after
![after](../master/after.png)

usage
-----
    java -jar CleanBadlyScannedFiles.jar [filename]
    
download
--------
[CleanBadlyScannedFiles.jar](../../raw/master/dist/CleanBadlyScannedFiles.jar)

patterns
--------
Patterns which should be removed can be added as a two-dimensional array of `ints`.

Value  | Color
-------------:| -------------
0  | White
1  | Black
-1 | Any
