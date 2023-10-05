package org.tungsten.client.util.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FilePickerUtil {

    public static void pickFile(FilePickCompletion onCompleted) {
        new Thread(() -> {
            try{
                String script =
                        """
                                Add-Type -AssemblyName System.Windows.Forms
                                $dialog = New-Object System.Windows.Forms.OpenFileDialog
                                $dialog.Filter = "Minecraft Plugin (*.jar)|*.jar"
                                $dialog.InitialDirectory = "C:\\"
                                $dialog.Title = "Select a Plugin"
                                $dialog.ShowDialog() | Out-Null
                                $file = $dialog.FileName
                                $filePath = Join-Path $env:APPDATA "tmppath.txt"
                                if ($file) {
                                    Set-Content -Path $filePath -Value $file
                                    exit 0
                                } else {
                                    exit 1
                                }
                                """;
                File scriptPath = new File(System.getenv("appdata") + "\\rom.ps1");
                if(scriptPath.exists()){
                    scriptPath.delete();
                    scriptPath.createNewFile();
                }
                FileWriter ps = new FileWriter(scriptPath);
                ps.write(script);
                ps.close();


                File deltaLocation = new File(System.getenv("appdata") + "\\tmppath.txt");
                if(deltaLocation.exists()){
                    deltaLocation.delete();
                }
                Process process = Runtime.getRuntime().exec("powershell -ExecutionPolicy Bypass -File " + scriptPath.getAbsolutePath());

                process.waitFor();

                if(process.exitValue() == 0) {
                    FileReader f = new FileReader(deltaLocation);
                    BufferedReader r = new BufferedReader(f);
                    String path = r.readLine();
                    System.out.println(path);
                    onCompleted.pickFiles(path);
                    r.close();
                    f.close();
                    deltaLocation.delete();
                }else{
                    onCompleted.pickFiles(null);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }).start();

    }
}
