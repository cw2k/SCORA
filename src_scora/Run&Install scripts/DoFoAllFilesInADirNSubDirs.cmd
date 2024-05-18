@prompt -$G

@set DirToProcess=d:\Downloads\Sibelius710b54\Output\Sibelius710b54

@set DirToSCORA=e:\Crack\Sibelius\Sibelius Scorch File converter\Sibelius Scorch File converter 1.0


::@set CurDir=%~dp0
::cd /d %CurDir%
cd /d %DirToProcess%

::for /?
for /R %%i in (*.*) do @(
	echo %%~nxi
	
	@cd /d %%~dpi
	java -jar "%DirToSCORA%\SCORA.jar" -noDecompress  "%%~nxi"
	if exist "%%~nxi.sib" (
		
		del "%%~nxi"
		ren "%%~nxi.sib" "%%~nxi"
	)
)

Pause