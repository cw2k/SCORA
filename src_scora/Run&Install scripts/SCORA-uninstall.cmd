@prompt -$G

@echo Scora Deinstallation (Ver 1)
@echo ============================
@echo.
@echo ==^> Deinstalling File Association...

:: Open *.sco with scora.jar
	 FType Sibelius.Scorch=
	 assoc .sco=Sibelius

@echo.
@echo ==^> Done !!!
@echo  Now doubleclick on a "*.sco" may open Sibelius (if it's installed)
@echo.
@echo.
@pause