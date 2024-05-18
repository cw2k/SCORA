Scorch-Away Version 1.2c - Readme
================================

Scorch-Away is made to converts 
Avid scorch (*.sco) music score file into 
Avid Sibelius file (*.sib).

Sibelius… is a score writer program, for Microsoft Windows, Mac OS, and RISC OS. 
Scorch… is a Web browser plug-in for playing and printing sheet music via the Internet or an iPad. 

Actually (*.sco) and (*.sib) are quite the same. The only thing is that the (*.sco) format was artificially change a little so it can't opened in Avid Sibelius anymore.
(^-use my patch to enable *.sco support for the Windows Version of Sibelius)
These *.sco can only be opened with more or less restriction in Scorch.

Installation:
-------------

-	SCORA-install.cmd   Associate (*.sco) with Scorch
-	SCORA-uninstall.cmd Associate (*.sco) with Sibelius

Try out SCORA.cmd. If it runs, everything is alright so far.

…else you should know that Scorch-Away requires the Java Runtime Environment to run. Download&install it here
http://www.java.com/de/download/manual.jsp

Usage Examples:
--------------

* Double click in 'Examples\' on
   HL_DDS_0000000000128674.sco
   ^- Scorch-Away is launched and creates
   HL_DDS_0000000000128674.sco.sib

* start the command prompt cmd.exe 
  HL_DDS_0000000000128674.sco
Or
  Java -jar SCORA.jar Examples\HL_DDS_0000000000128674.sco
  
  SCORA.cmd -noDecompress CircleofFifths.sib 
  
  Rename "db.ideas" -> "db.ideas.sco" & doubleclick it.


To apple and linux users
------------------------

I wrote SCORA in Java so it may be free to also run on other platforms.
However I haven't tested that yet. And as an old software developers saying says:

What's not tested – don't work.

It's probably like that.  However somehow it should and will work.
I'll be very happy about every feedback (email is at the bottom)
About that it works, improvements, asking for help…


History
--------
1.2 
	* Bugdfix: Downgraded 'java/nio/file/Paths' to String so now SCORA also runs with Java 1.6

1.1 
	* Extracts embedded PDF from *.sco
	
1.0 
	* Bugdfix: Files with more than one chunk are handle correctly
	* Speed improvement through use of buffered Input/Outputstreams
	* -HTML, -chunks, -verbose parameter
	
0.9 PreRelease
	* First version


http://deioncube.in/files/?dir=cw2k/Sibelius Patch & Scorch File converter
