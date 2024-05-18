@prompt -$G

set CurDir=%~dp0
set JAR=SCORA.jar


if not exist "%CurDir%%JAR%" (
	@echo E R R O R : %JAR% was not found !
	@echo             %JAR% must be in the same directory like this file.^)
	@echo.
	@echo Possible reason: 
	@echo You ran me by just doubleclick on me in WinRar.
	@echo Please extract all files to some directory and try again.
	@echo.
	@goto Exit
)

:: Set additional options for Scora -like '-noHTML -noDecompress' - here:
	 @set ScoraOptions=

@echo Scora Java Launcher...
@echo.


java -jar  %ScoraOptions% "%CurDir%%JAR%" %*

:Exit
::@pause
