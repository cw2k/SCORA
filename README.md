# SCORA
SCORch Away is a Converter to turn Scorch viewer files (*.sco) back into AVID-Sibelius (*.sib).  
A SIB file is a musical score created by Avid Sibelius, a popular music notation program.

## Requirements 
JRE must be installed. [Java Runtime Envirmoment](https://java.com/download/manual.jsp). 
**Tested with JRE 8 411**

## How To use
__GUI_Launcher.exe__ is a Grapical User Interface for Windows written in VB6. It requires SCORA.jar.
Usage is self explainable.
![Scora GUI Screenshot](https://abload.de/img/2013-03-13-08.38.23cax9uuf.png)

__SCORA.jar__ is the core and the only file you really need.

On your commandline/Terminal run this:

`java -jar SCORA.jar`

... and you should see the following help:

	Scorch-Away Version 1.2
	=======================
	
	Converts a Avid Scorch (*.sco) music score file into Avid Sibelius file (*.sib).
	
	 Now you can edit open & print it in Avid Sibelius without any restriction ! 
	 ... or edit the scorch restriction (at the end of the file) with a (Hex-)Editor 
	 and  open it in the scorch webplugin via the generate *.htm.
	
	 Note: You can also open a *.sib to remove its encryption and compression.
	      Now it's open to analyze/customization!
	      ... and you gain a better compression rate when packing it with
	      Rar or 7-zip/LZMA.
	
	
	usage: Scorch.jar <*.sco|*.sib file>  [<OutputfileName>]
	
	
	options:
	 -noDecompress   Don't decompress the decrypted data
         -NoUnlock       Keep Scorch options as they are.
	 -chunks  (requires: -noDecompress)  Redirects decompressed chunk data into new 
	                                     files (useful for *.idea or *.lib and *.sco 
	                                     with embedded PDF)
	 -verbose  Shows addition information
	 -HTML     Create Html to run the file in Scorch

Running:
`java -jar SCORA.jar example\smd_137443.sco -verbose`
Will create `smd_137443.sco.sib` 
and as well modify and unlock the source file `smd_137443.sco` 
If you do not want this add the `-NoUnlock` option to keep the source file untouched.

List of some *.sco on 
[ sheetmusicdirect.com via archive.org](https://web.archive.org/web/*/https://www.sheetmusicdirect.com/scorches*)
to download and testing.

sco-PDF Example:
[smd_h_1073902WBRe72Gb0O.sco from sheetmusicdirect.com] (https://www.sheetmusicdirect.com/scorches/smd_h_1073902WBRe72Gb0O.sco)
If there is a PDF the the created is of no use and should be deleted.

[COME TO THE MUSIC](https://www.sheetmusicdirect.com/de-DE/se/ID_No/158473/Product.aspx)

Wow cool they still (@2024) have the old scorch on their website:
    <object id="Object1" classid="clsid:A8F2B9BD-A6A0-486A-9744-18920D898429"
            codebase="http://www.sibelius.com/download/software/win/ActiveXPlugin.cab#version="
            width="550" height="715">
        <param name="src" value="https://www.sheetmusicdirect.com/scorches/smd_h_1073902WBRe72Gb0O.sco"/>
        <param name="type" value="application/x-sibelius-score"/>
        <param name="scorch_arg_1" value="158473"/>
	...


### Grabbing *.sco Scorch files from a site.

The easiest way is to use these SCORA [bookmarklets](http://wikipedia.org/wiki/Bookmarklet)

To Install a bookmarklet apply the following steps:

1. Copy the code in the selected code snippet.
2. Create a new bookmark / favourite in your browser. Name it like the name text above the code. For the URL part paste(ctrl + v) the code you copied in step 1. 
3. Click "Done"

**Get SCO now**
```javascript
javascript:window.location=document.body.textContent.match(/[^"'>\s]+\.(sco)/gmi)[0]
```
that is the Instant download version 

**Make SCO link**
```javascript
javascript:var d=document.body;var f=(d.textContent.match(/[^"'>\s]+\.(sco)/gmi)||alert("No SCO found!"))[0];var e = document.createElement('a');e.text='Download *.sco file';e.href=f;d.prepend(e);
```
that version creates a download on the top of the page you can click on.

To Use:
1. Open a website that has a *.sco on it. (Uses the Scorch player)
2. Click the Bookmark you created before.

Alternative workaround is to view the html-pages source (Hotkey is mostly Ctrl+U) and search for ".sco" there.

#### Bookmarklet source code

**Make SCO link**
```javascript
// Unfortunately DOM does not parse the script tag.
// So that is how far you'll get via selectors: 
// document.querySelector("script[id*='scorchObjectTemplate']")
//
// So the workaround is a plain Full text grep on the site
var d = document.body;
var f = (d.textContent.match(/[^"'>\s]+\.(sco)/gmi)  || alert("No SCO found!") )[0]; 

var e = document.createElement('a');
e.text  = 'Download *.sco file';
e.href  = f;

d.prepend(e);
```
**Get SCO now**
```javascript
	window.location = 
		document.body.textContent.match(/[^"'>\s]+\.(sco)/gmi)[0])
	    .src;
```

### Browser with NSAPI support to use Scorch player on the sites.

 Mozilla has dropped the support of NPAPI plugins from Mozilla Firefox version 52.
"[Pale Moon](https://www.palemoon.org/download.shtml) supports NPAPI plug-ins. Unlike Firefox, we will not be deprecating or removing support for these kinds of plug-ins."
from [askubuntu.com](https://askubuntu.com/questions/905910/is-there-any-web-browser-with-npapi-plugins-support)


So install palemoon 32-Bit Version.
[Scorch620NSPluginInstaller](http://hub.sibelius.com/download/software/win/Scorch620NSPluginInstaller.exe)
If installation hangs on VCredist kill installer.exe in Taskmanager.
If installation succeed it there should be the file
"c:\Program Files (x86)\Sibelius Software\Scorch\ScorchAxPlugin.dll"
Incase you somehow manually extract the files from the installer.
run regsvr32.exe ScorchAxPlugin.dll it'll invoke COM-Registation ( call API-export DllRegisterServer)
... and save it's GUID and the path to the Registry.
palemoon and others will then find it by its GUID.


okay now you can test it via
`java -jar SCORA.jar -verbose -HTML example\smd_137443.sco`

load the created smd_137443.sco.html in palemoon Browser.
Pay attention that you installed the 32-Bit Version of palemoon Browser ! ).


Okay on https://www.sheetmusicdirect.com/de-DE/se/ID_No/114818/Product.aspx it still seems to not work.
Well if scorch object is inside a script tag, it can't run.
This Bookmarklet will remove that script tag

**Reactivate SCO**
```javascript
javascript:var o=document.querySelector("script[id*='scorchObjectTemplate']")||alert("No SCO found!");o.outerHTML=o.innerHTML;undefined
```

### From sibelius.com helpcenter

__[SOLVED] Converting *.sco -> *.sib__
**open free .sco files in sibelius
**is there a possibility to open free .sco (scorch files) in sibelius to edit it? i want to modify some arrangement i found on sibeliusmusic.com for my musicclass ensembles. now i have to print out the sheets and scan with photoscore to have the possibility of editing in sibelius. it is not comfortable. 

Of course you can open .sco again in Sibelius.
But it requires some 'magic' to undo da bad stuff and unwrap the 'CCSCORCH'-Layer. :rolleyes:
In early days I patches sibelius.exe(*## Sibelius710b54_CW2K_Edition.7z*) to also open *.sco files.
However to cover the cases were there is an pdf wrapped into a *.sco and just for fun I made a special program call 'SCORA':

GUI is windows(VB6) but the main part(SCORA.jar) of the converter is in java so it could be run on both PC and MAC.
**SCORA **is short for **Scorch away** and will completely unwrap *.sco files. 

And here's a shortcut for the Scorch-player *Windows only*:
ScorchPatch1.0_CW2K.7z
It'll remove **all restrictions** and allow to **view&print** all pages as well as **save** the output as *.midi or *.sco.



Scorch-player is not working since Webkit (Chrome, Opera, Safari) kicked out NSAPI-support. I recommand to use of firefox or Internet explorer that may still has it.

However you don't need the Scorch-player. Just open website source( in Chrome Ctrl+U) and search for **.sco**

		<embed id="ScorchPlugin" 
			src="http://www.sheetmusicdirect.com/scorches/smd_h_0000000000097761.sco"
			width="600"
			height="800"
			align="BOTTOM" 
			type="application/x-sibelius-score"
			pluginspage="http://www.sibelius.com/cgi/plugin.pl"

... and there he is the **SCO-Download-Link**

`http://www.sheetmusicdirect.com/scorches/smd_h_0000000000097761.sco`

Download it to were SCORA is (run cmd_exe) and type in (Copy&paste)

`SCORA.cmd smd_h_0000000000097761.sco`

You can also just double click on some ***.sco** and tell windows to open these files with **SCORA.cmd**
## (or rightclick and choose **Open with.../Choose standard program** to bring up the 'open with dialog' again)
.
**now cross your fingers. I hope all that java stuff is installed correctly on ya system so the converter'll work**
->**smd_h_0000000000097761.sib** ( or sometimes *.pdf) will come out that'll open fine in Sibelius.:D
