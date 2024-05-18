::
:: Wow congratulations :) This file made it into ya Editor !!!
::
:: Set additional options for Scora -like '-HTML -noDecompress' - here:
	 @set ScoraOptions=-HTML -chunks
::
::-------------------------------------------------------------------------
::
:: Command prompt: '->' 
@prompt -$G
@cd /d "%~dp0"

@echo Scora Installation (Ver 1)
@echo ==========================
@echo.
@echo ==^> Installing File Association...

	 
:: Open *.sco with scora.jar
::
::	 FType Sibelius.Scorch=cmd /k java -jar "%~dp0scora.jar" %ScoraOptions% "%%1" %%*   >nul
	 FType Sibelius.Scorch="%~dp0scora.cmd" %ScoraOptions% "%%1" %%*   >nul
::  ^- open and wait

::	 FType Sibelius.Scorch=java -jar "%~dp0scora.jar" %ScoraOptions% "%%1" %%*   >nul
	 assoc            .sco=Sibelius.Scorch

::	 assoc            .sib=Sibelius.Scorch
	 assoc            .lib=Sibelius.Scorch
	 assoc            .ideas=Sibelius.Scorch


@echo.
@echo ==^> Done !!!
@echo  You can now simply doubleclick on a "*.sco" 
@echo  to convert it into a "*.sib" File.
@echo.
@echo  Hint: Open this file in a editor to set additional options for Scora.
2.sco
::Reg delete "HKCU\Software\Microsoft\Windows\CurrentVersion\Explorer\FileExts\.sco" /f
::@pause
