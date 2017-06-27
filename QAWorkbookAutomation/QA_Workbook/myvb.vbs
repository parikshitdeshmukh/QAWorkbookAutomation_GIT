Set objExcel = CreateObject("Excel.Application")
Dim url, stage, prod, member, jobID
Dim fname, fullName
objExcel.Application.ScreenUpdating = False


url =  WScript.Arguments.Item(0)
stage =  Wscript.Arguments.Item(1)
prod =  Wscript.Arguments.Item(2)
member = Wscript.Arguments.Item(3)
jobID = Wscript.Arguments.Item(4)
'fname = "C:\work\QAWorkbookAutomation\QA_Workbook\new" & "\" & Member & "\" & Member & ".xlsm"


Set objWorkbook = objExcel.Workbooks.Open(url,0, False)


'Set Arg = WScript.Arguments
'var1 = Arg(0)
'var2 = Arg(1)
objExcel.Application.DisplayAlerts = False
objExcel.Run "Trend_Sheet",CStr(stage),CStr(prod),CStr(member),CStr(jobID)
'objExcel.ActiveWorkbook.Save
'Workbooks("var1").CheckIn True
'objExcel.ActiveWorkbook.SaveAs "C:\work\QAWorkbookAutomation\QA_Workbook\new\hero.xlsm",xlOpenXMLWorkbookMacroEnabled
'objExcel.ActiveWorkbook.Close
objExcel.ActiveWorkbook.Close
objExcel.Application.Quit
objExcel.Application.ScreenUpdating = True
Set objWorkbook=nothing
Set objExcel=nothing



