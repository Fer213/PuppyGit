package com.catpuppyapp.puppygit.utils

import android.content.Context
import com.catpuppyapp.puppygit.play.pro.R
import com.github.git24j.core.Libgit2
import java.io.File

object Lg2Utils {
    private const val libgit2HomeDirName = "lg2home"
    private const val sshDirName = ".ssh"
    private const val sshKnownHostsFileName = "known_hosts"
    private val known_hostsRawId = R.raw.known_hosts

    private lateinit var lg2Home: File
    private lateinit var sshDir: File
    private lateinit var knownHostsFile:File
    
    fun init(homeBaseDirPath:File, appContext: Context) {
        lg2Home=createDirIfNonexists(homeBaseDirPath, libgit2HomeDirName)
        sshDir=createDirIfNonexists(lg2Home, sshDirName)
        knownHostsFile = File(sshDir.canonicalPath, sshKnownHostsFileName)

        createKnownHostsIfNonExists(appContext)

        // make ssh can find the known_hosts file
        Libgit2.optsGitOptSetHomedir(lg2Home.canonicalPath)
    }
    
    fun getLg2Home():File {
        if(lg2Home.exists().not()) {
            lg2Home.mkdirs()
        }

        return lg2Home
    }

    
    fun createKnownHostsIfNonExists(appContext: Context) {
        if(knownHostsFile.exists()) {
            return
        }

        knownHostsFile.parentFile?.mkdirs()
        knownHostsFile.createNewFile()

        //从app res读取证书内容到文件
        val inputStream = appContext.resources.openRawResource(known_hostsRawId)
        val outputStream = knownHostsFile.outputStream()
        inputStream.use { input ->
            outputStream.use { output->
                var b = input.read()
                while(b!=-1) {
                    output.write(b)
                    b = input.read()
                }
            }
        }
    }

    fun resetKnownHostFile(appContext: Context){
        knownHostsFile.delete()
        createKnownHostsIfNonExists(appContext)
    }
    
    fun getKnownHostsFile(appContext: Context):File {
        createKnownHostsIfNonExists(appContext)

        return knownHostsFile
    }
    
//    fun addRulesToKnowHosts(rules:StringBuilder) {
//        //TODO()
//    }
}