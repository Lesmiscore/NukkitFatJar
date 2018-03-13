package com.nao20010128nao.NukkitFatJar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

import java.util.zip.ZipFile

class Main implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task("nukkitFatJar", type: Jar) {
            it.from {
                project.configurations.compile.findAll { !isNukkit(it) }.collect {
                    it.isDirectory() ? it : project.zipTree(it)
                }
            }
            it.with project.jar
        }
    }

    static boolean isNukkit(File f) {
        if (f.file) {
            def zf = null
            try {
                zf = new ZipFile(f)
                return zf.getInputStream(zf.getEntry("cn/nukkit/Nukkit.class")).bytes
            } finally {
                zf?.close()
            }
        } else if (f.isDirectory()) {
            return new File(f, "cn/nukkit/Nukkit.class").exists()
        } else {
            return false
        }
    }
}
