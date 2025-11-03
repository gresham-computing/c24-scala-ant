import java.util.zip.*


String zipFileName = "c24-ctc-streamtransformerplugin-dependencies.zip"
String inputDir = "."

ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))

println "Starting..."
addDirectory(new File(inputDir), zipFile, new ArrayList<String>())

println "Closing..."
zipFile.close()

def addZipEntry(File file, ZipOutputStream zipFile, ArrayList<String> fileNames) {
    if (file.getName().endsWith(".jar") && !file.getName().startsWith("c24") && !fileNames.contains(file.getName())) {
        zipFile.putNextEntry(new ZipEntry(file.name))
        def buffer = new byte[file.size()]
        file.withInputStream {
            zipFile.write(buffer, 0, it.read(buffer))
        }
        zipFile.closeEntry()
        fileNames.add(file.name)
        println "Adding entry $file.absolutePath"
    }
}

def addDirectory(File directory, ZipOutputStream zipFile, ArrayList<String> fileNames) {
    directory.eachFile() { file ->
        if (!file.name.startsWith(".")) {
            if (file.isDirectory()) {
                addDirectory(file, zipFile, fileNames)
            }
            else if (directory.name.equals("all-dependencies")) {
                addZipEntry(file, zipFile, fileNames)
            }
        }
    }
}