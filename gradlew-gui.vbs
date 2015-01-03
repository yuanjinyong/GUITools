set objShell = wscript.createObject("wscript.shell")
currDir = createobject("Scripting.FileSystemObject").GetFile(Wscript.ScriptFullName).ParentFolder.Path
objShell.run currDir & "\gradlew.bat --gui", 0