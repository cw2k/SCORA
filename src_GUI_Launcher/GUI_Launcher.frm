VERSION 5.00
Begin VB.Form MainForm 
   Caption         =   "SCORA_GUI_Launcher"
   ClientHeight    =   8550
   ClientLeft      =   225
   ClientTop       =   825
   ClientWidth     =   10185
   Icon            =   "GUI_Launcher.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   ScaleHeight     =   8550
   ScaleWidth      =   10185
   StartUpPosition =   3  'Windows Default
   Begin VB.CheckBox chk_AutoLaunch 
      Alignment       =   1  'Right Justify
      Caption         =   "AutoLaunch"
      Height          =   315
      Left            =   240
      TabIndex        =   15
      Tag             =   "-HTML"
      ToolTipText     =   "Launch after file drop or browse"
      Top             =   2040
      UseMaskColor    =   -1  'True
      Value           =   1  'Checked
      Width           =   1815
   End
   Begin VB.Frame fr_options 
      Caption         =   "Options"
      Height          =   975
      Left            =   2280
      TabIndex        =   10
      Top             =   1440
      Width           =   4095
      Begin VB.CheckBox chk_opt_NoImages 
         Caption         =   "NoImages"
         Height          =   315
         Left            =   2760
         TabIndex        =   17
         Tag             =   "-NoImages"
         ToolTipText     =   "Disables search & extraction of pictures"
         Top             =   240
         UseMaskColor    =   -1  'True
         Value           =   1  'Checked
         Width           =   1215
      End
      Begin VB.CheckBox chk_opt_noUnlock 
         Caption         =   "noUnlock"
         Height          =   195
         Left            =   2760
         TabIndex        =   16
         Tag             =   "-noUnlock"
         ToolTipText     =   "keeps Scorch restrictions options as they are"
         Top             =   600
         UseMaskColor    =   -1  'True
         Value           =   1  'Checked
         Width           =   1095
      End
      Begin VB.CheckBox chk_opt_verbose 
         Caption         =   "verbose"
         Height          =   195
         Left            =   1200
         TabIndex        =   14
         Tag             =   "-verbose"
         ToolTipText     =   "Shows addition information"
         Top             =   600
         UseMaskColor    =   -1  'True
         Width           =   1335
      End
      Begin VB.CheckBox chk_opt_chunks 
         Caption         =   "Chunks"
         Height          =   195
         Left            =   240
         TabIndex        =   13
         Tag             =   "-chunks"
         ToolTipText     =   "Saves decompressed chunk data into new files"
         Top             =   600
         UseMaskColor    =   -1  'True
         Width           =   855
      End
      Begin VB.CheckBox chk_opt_noDecompress 
         Caption         =   "noDecompress"
         Height          =   315
         Left            =   1200
         TabIndex        =   12
         Tag             =   "-noDecompress"
         ToolTipText     =   "Keeps compression"
         Top             =   240
         UseMaskColor    =   -1  'True
         Width           =   1455
      End
      Begin VB.CheckBox chk_opt_HTML 
         Caption         =   "HTML"
         Height          =   315
         Left            =   240
         TabIndex        =   11
         Tag             =   "-HTML"
         ToolTipText     =   "Create Html to run the file in Scorch"
         Top             =   240
         UseMaskColor    =   -1  'True
         Width           =   975
      End
   End
   Begin VB.Frame Fr_DropFilesHere 
      BackColor       =   &H80000005&
      BorderStyle     =   0  'None
      Height          =   2775
      Left            =   1320
      OLEDropMode     =   1  'Manual
      TabIndex        =   8
      Top             =   3960
      Width           =   6615
      Begin VB.Label lbl_DropFilesHere 
         Alignment       =   2  'Center
         Appearance      =   0  'Flat
         AutoSize        =   -1  'True
         BackColor       =   &H80000005&
         BackStyle       =   0  'Transparent
         Caption         =   "< Drop files here >"
         BeginProperty Font 
            Name            =   "Verdana"
            Size            =   24
            Charset         =   0
            Weight          =   400
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         ForeColor       =   &H80000000&
         Height          =   570
         Left            =   885
         OLEDropMode     =   1  'Manual
         TabIndex        =   9
         Top             =   960
         Width           =   4695
      End
   End
   Begin VB.CheckBox Chk_verbose 
      Caption         =   "Verbose LogOutput"
      Height          =   195
      Left            =   7440
      MaskColor       =   &H8000000F&
      TabIndex        =   6
      Top             =   2160
      Visible         =   0   'False
      Width           =   1785
   End
   Begin VB.Timer Timer_TriggerLoad_OLEDrag 
      Enabled         =   0   'False
      Interval        =   100
      Left            =   240
      Top             =   1560
   End
   Begin VB.TextBox Txt_scora 
      ForeColor       =   &H80000006&
      Height          =   285
      Left            =   240
      TabIndex        =   4
      Text            =   "java -jar ""SCORA.jar""   ""{File}"""
      ToolTipText     =   "Supported placeholdes  {curPath} and {File}"
      Top             =   1080
      Width           =   8295
   End
   Begin VB.CommandButton cmd_Launch 
      Caption         =   "Launch"
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   495
      Left            =   240
      TabIndex        =   1
      Top             =   1560
      Width           =   1815
   End
   Begin VB.CommandButton cmd_Browse 
      Caption         =   "..."
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   400
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   9240
      TabIndex        =   0
      Top             =   480
      Width           =   495
   End
   Begin VB.Frame Frame1 
      Caption         =   "*.sco"
      Height          =   855
      Left            =   120
      TabIndex        =   2
      Top             =   120
      Width           =   9855
      Begin VB.ComboBox cb_ScoFiles 
         Height          =   315
         Left            =   240
         TabIndex        =   5
         Top             =   360
         Width           =   8655
      End
   End
   Begin VB.PictureBox pic_Logo 
      Appearance      =   0  'Flat
      AutoSize        =   -1  'True
      BackColor       =   &H80000005&
      BorderStyle     =   0  'None
      FillColor       =   &H00FFFFFF&
      ForeColor       =   &H80000008&
      Height          =   1155
      Left            =   6480
      Picture         =   "GUI_Launcher.frx":1442
      ScaleHeight     =   1155
      ScaleWidth      =   3540
      TabIndex        =   7
      Top             =   840
      Width           =   3540
   End
   Begin VB.ListBox List_Cmd 
      BeginProperty Font 
         Name            =   "System"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   5820
      ItemData        =   "GUI_Launcher.frx":249F
      Left            =   120
      List            =   "GUI_Launcher.frx":24A1
      OLEDropMode     =   1  'Manual
      TabIndex        =   3
      Top             =   2520
      Width           =   9975
   End
   Begin VB.Menu mi_SaveLog 
      Caption         =   "&SaveLog"
   End
   Begin VB.Menu mi_CheckForUpdate 
      Caption         =   "Check For &Update"
   End
   Begin VB.Menu mi_mi_Forum 
      Caption         =   "&Forum"
   End
End
Attribute VB_Name = "MainForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Public Path

Private LogData()

Private WithEvents Console  As Console
Attribute Console.VB_VarHelpID = -1





Private Sub HandleOption(chk As CheckBox)
   With chk
   
      Dim Optiontext
      Optiontext = .Tag
      
      
      Dim New_Txt_scora$
      
      New_Txt_scora = Txt_scora.Text
      ReplaceDo New_Txt_scora, Optiontext, ""
      New_Txt_scora = RTrim(New_Txt_scora)
      
      Dim Enabled As Boolean
      Enabled = (.value = vbChecked)
      
      If Enabled Then
         Txt_scora = New_Txt_scora & " " & Optiontext
      Else
         Txt_scora = New_Txt_scora
      End If
      

      
      
   End With
End Sub



'//////////////////////////////////////////////////////////////////////
'//   Options
'//
Private Sub chk_opt_Init()
   HandleOption chk_opt_chunks
   HandleOption chk_opt_HTML
   HandleOption chk_opt_noDecompress
   HandleOption chk_opt_NoImages
   HandleOption chk_opt_noUnlock
   HandleOption chk_opt_verbose
End Sub

Private Sub mi_CheckForUpdate_Click()
   openURL ("https://github.com/cw2k/SCORA")
End Sub

Private Sub mi_mi_Forum_Click()
   openURL ("http://www.sibeliusforum.com//viewtopic.php?f=9&t=69659")
End Sub

Sub openURL(url$)
   Dim hProc&
   hProc = ShellExecute(0, "open", url, "", "", 1)
End Sub

Private Sub chk_opt_chunks_Click()
   HandleOption chk_opt_chunks
End Sub

Private Sub chk_opt_HTML_Click()
   HandleOption chk_opt_HTML
   
 ' Without HTML; Unlocking makes not much sense
   chk_opt_noUnlock.value = vbUnchecked
   
End Sub

Private Sub chk_opt_noDecompress_Click()
   HandleOption chk_opt_noDecompress
   
 ' Compressed Data can not be searched or Unlocked makes not much sense
   chk_opt_noUnlock.value = vbChecked
   chk_opt_NoImages.value = vbChecked
   
End Sub

Private Sub chk_opt_NoImages_Click()
   HandleOption chk_opt_NoImages
End Sub

Private Sub chk_opt_noUnlock_Click()
   HandleOption chk_opt_noUnlock
End Sub

Private Sub chk_opt_verbose_Click()
   HandleOption chk_opt_verbose
End Sub


'//////////////////////////////////////////////////////////////////////
'//   Browse
'//
Sub cmd_Browse_Click()

  With New cOpenSaveDialog
   .Flags = OFN_ALLOWMULTISELECT ' + OFN_EXPLORER
   .DialogTitle = "Select one (or more) files"
   .Filter = "All Files|*.*|" & _
             "Sibelius scorch Files|*.sco"
      If .ShowOpen(hWnd) Then

         Dim item
         For Each item In .FileNames
            AddFile Trim(item)
         Next
         
         
         Timer_TriggerLoad_OLEDrag.Enabled = True
         
      End If
      
   End With
End Sub

'//////////////////////////////////////////////////////////////////////
'//   Launch
'//


Sub Launch_Init(FileName, Params$)
   
   Hide_DropFilesHereLbl
   
   LogClean
   
   
   MyChDir App.Path
   
   FileName = Split(Txt_scora)(0)
   
   Params = Mid(Txt_scora, 1 + Len(FileName) + 1)
   ReplaceDo Params, "{curPath}", CurDir
   
  'Handle .Text of cb_ScoFiles
   If cb_ScoFiles.ListCount <= 0 Then
      If FileExists(cb_ScoFiles.Text) Then
         cb_ScoFiles.AddItem cb_ScoFiles.Text
      End If
   End If
   

End Sub

Sub cmd_Launch_Click()
On Error GoTo err
   
   Dim Params$
   
   Launch_Init FileName, Params
   
   
   
   Dim ScoFileName As New ClsFilename
   
   Dim item As New ClsFilename, i
   For i = 0 To cb_ScoFiles.ListCount - 1
      item = cb_ScoFiles.List(i)
      
    ' Keep (old) dir if dir name is missing
      If item.Path = "" Then
         ScoFileName.NameWithExt = item.NameWithExt
      Else
         ScoFileName = item
      End If
      
      LogTxt Quote(ScoFileName.FileName), "==> "
      LogTxt " "
      
      
      
      'MyChDir item.Path
      
      Dim CommandToExec$
      CommandToExec = Replace(Params, "{File}", ScoFileName.FileName)
      
      
      LogTxt "CurDir: " & CurDir

      LogVerbose "FileName: " & FileName
      LogTxt "CommandToExec: " & CommandToExec

      
      
      Dim Retval$
      Retval = Console.ShellExConsole("" & FileName, CommandToExec)
      
   Next
      
   ClearFiles

   
   
err:
  Select Case (err.Number)
  Case 0
  Case 13
       LogErr cb_ScoFiles & " not found!"
       Resume Next
       
  Case Else
       LogErr H32(err.Number) & " " & err.Description
       Resume Next
       
   End Select
   
   
End Sub


Sub MyChDir(NewDir)
   If CurDir <> NewDir Then
      ChDrive NewDir
      ChDir NewDir
      
      LogVerbose "CurDir: " & CurDir
      
   End If
End Sub

'//////////////////////////////////////////////////////////////////////
'//   d
'//

Private Sub Console_OnDone(ExitCode As Long)
   LogVerbose "ExitCode:" & ExitCode
   LogLine
End Sub

Private Sub Console_OnOutput(TextLine As String, ProgramName As String)
   LogTxt TextLine, "    "
End Sub

'//////////////////////////////////////////////////////////////////////
'//   Form Load
'//

Sub Form_Load()

   Set Console = New Console
   
   Caption = Caption & " " & App.Major & "." & App.Minor & " build(" & App.Revision & ")"
   
   
   chk_opt_Init
'   '   cb_ScoFiles = GetSetting("SCORA_GUI_Launcher", "Settings", "path", cb_ScoFiles)
   
   '  Chk_Autopaste.value = GetSetting("SCORA_GUI_Launcher", "Settings", "autopaste", Chk_Autopaste)
   'DeleteSetting "Anw1", "Startup"
   Exit Sub
err:
End Sub



Private Sub Form_Unload(Cancel As Integer)
'  SaveSetting "Launchkill", "Settings", "path", cb_ScoFiles
'  SaveSetting "Launchkill", "Settings", "times", txt_times
'  SaveSetting "Launchkill", "Settings", "wait", txt_wait
'  SaveSetting "Launchkill", "Settings", "autopaste", Chk_Autopaste.value
End Sub



'Private Sub Label3_Click()
'
'If vbYes = MsgBox("Remove " & App.Title & " settings for Registry" & vbCrLf & _
'         App.Title & " will exit after this.", vbQuestion + vbYesNoCancel) _
'Then
'  On Error Resume Next
'  DeleteSetting "Launchkill", "Settings"
'  End
'End If
'
'
'End Sub


Public Sub LogErr(Text As String, Optional LinePrefix$ = "")
   LogTxt Text
End Sub

Public Sub LogVerbose(Text As String, Optional LinePrefix$ = "")
   If Chk_verbose.value = vbChecked Then
      LogTxt Text
   End If
End Sub

Public Sub LogTxt(Text As String, Optional LinePrefix$ = "")

   Dim item
   For Each item In Split(Text, vbCrLf)
      List_Cmd.AddItem LinePrefix & item
      
      ArrayAdd LogData, LinePrefix & item

      
   Next
   
End Sub

Public Sub LogLine()
   LogTxt String(80, "=")
End Sub

Public Sub LogClean()
    List_Cmd.Clear
   
'   LogData.Clear
    ArrayDelete LogData
End Sub


Private Sub Form_Resize()
On Error Resume Next
   
   Me.Height = Max(Me.Height, 9090)
   Me.Width = Max(Me.Width, 10425)
   
   With List_Cmd
   
   .Height = Me.Height - 550 - .Top
   .Width = Me.Width - 560 - .Left
   End With
End Sub

Private Sub List_Cmd_DblClick()
   frmLogView.txtlog = Replace( _
                        Log_GetData, _
                        vbNullChar, ".")
   frmLogView.Show
End Sub


Public Function Log_GetData$()
   Log_GetData = Join(LogData, vbCrLf)
End Function



'//////////////////////////////////////////////////////////////////////
'//   DropFilesHere Label
'//
Private Sub Fr_DropFilesHere_OLEDragDrop(Data As DataObject, Effect As Long, Button As Integer, Shift As Integer, X As Single, Y As Single)
   File_DragDrop Data
End Sub

Private Sub lbl_DropFilesHere_OLEDragDrop(Data As DataObject, Effect As Long, Button As Integer, Shift As Integer, X As Single, Y As Single)
   File_DragDrop Data
   
End Sub

Private Sub List_Cmd_OLEDragDrop(Data As DataObject, Effect As Long, Button As Integer, Shift As Integer, X As Single, Y As Single)
   File_DragDrop Data
End Sub


Public Sub Hide_DropFilesHereLbl()
   Fr_DropFilesHere.Visible = False
End Sub

Private Sub File_DragDrop(Data As DataObject)
   
   On Error GoTo Combo_Filename_OLEDragDrop_err
   
   Dim item
   For Each item In Data.Files
      AddFile Trim(item)
   Next
   
   Timer_TriggerLoad_OLEDrag.Enabled = True
   

Combo_Filename_OLEDragDrop_err:
Select Case err
Case 0

Case Else
   LogErr "-->Drop'n'Drag ERR: " & err.Description

End Select

End Sub


'//////////////////////////////////////////////////////////////////////
'//   AddFile
'//
Public Sub AddFile(FileName$)
   cb_ScoFiles.AddItem FileName
   cb_ScoFiles.ListIndex = cb_ScoFiles.ListCount - 1
End Sub

Public Sub ClearFiles()
     
  'clean files
   cb_ScoFiles.Clear
   
End Sub



Private Sub mi_SaveLog_Click()
   Dim fileNameLog As New ClsFilename
   fileNameLog = CurDir() & "\Scora.log"
   
   FileSave fileNameLog.FileName, Log_GetData()
   LogTxt "Logdata saved to: " & fileNameLog
   
End Sub


Private Sub Timer_TriggerLoad_OLEDrag_Timer()
   Timer_TriggerLoad_OLEDrag.Enabled = False
   
   If chk_AutoLaunch.value = vbChecked Then _
      cmd_Launch_Click
      
   
End Sub


