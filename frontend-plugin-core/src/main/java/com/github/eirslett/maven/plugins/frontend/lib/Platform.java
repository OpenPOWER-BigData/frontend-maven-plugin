package com.github.eirslett.maven.plugins.frontend.lib;

//enum Architecture { x86, x64;
//    public static Architecture guess(){
//        return System.getProperty("os.arch").contains("64") ? x64 : x86;
//    }
//}
enum Architecture {
	x86, x64, ppc64le;
	public static Architecture guess() {
		String arch = System.getProperty("os.arch");
		if (arch.equals("ppc64le")) {
			return ppc64le;
		} else {
			return arch.contains("64") ? x64 : x86;
		}
	}
}

enum OS { Windows, Mac, Linux, SunOS;

    public static OS guess() {
        final String osName = System.getProperty("os.name");
        return  osName.contains("Windows") ? OS.Windows :
                osName.contains("Mac") ? OS.Mac :
                        osName.contains("SunOS") ? OS.SunOS :
                                OS.Linux;
    }

    public String getCodename(){
        if(this == OS.Mac){
            return "darwin";
        } else if(this == OS.Windows){
            return "windows";
        } else if(this == OS.SunOS){
            return "sunos";
        } else {
            return "linux";
        }
    }
}

class Platform {
    private final OS os;
    private final Architecture architecture;

    public Platform(OS os, Architecture architecture) {
        this.os = os;
        this.architecture = architecture;
    }

    public static Platform guess(){
        OS os = OS.guess();
        Architecture architecture = Architecture.guess();
        return new Platform(os,architecture);
    }

    public String getCodename(){
        return os.getCodename();
    }

    public boolean isWindows(){
        return os == OS.Windows;
    }

    public boolean isMac(){
        return os == OS.Mac;
    }

    public String getLongNodeFilename(String nodeVersion) {
        if(isWindows()){
            return "node.exe";
        } else {
            return "node-" + nodeVersion + "-" + this.getNodeClassifier();
        }
    }

    public String getNodeDownloadFilename(String nodeVersion) {
        if(isWindows()) {
            if(architecture == Architecture.x64){
                if (nodeVersion.startsWith("v0.")) {
                    return nodeVersion+"/x64/node.exe";    
                } else {
                    return nodeVersion+"/win-x64/node.exe";
                }
            } else {
                if (nodeVersion.startsWith("v0.")) {
                	return nodeVersion + "/node.exe";
                } else {
                    return nodeVersion+"/win-x86/node.exe";
                }
            }
        } else {
            return nodeVersion + "/" + getLongNodeFilename(nodeVersion) + ".tar.gz";
        }
    }

    public String getNodeClassifier() {
        return this.getCodename() + "-" + this.architecture.name();
    }
}
