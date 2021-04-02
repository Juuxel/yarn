package net.fabricmc.yarn.buildlogic.task

import de.undercouch.gradle.tasks.download.DownloadAction
import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
class DownloadTask extends DefaultTask {
    @Input
    Property<URL> sourceUrl = project.objects.property(URL)

    @OutputFile
    RegularFileProperty targetFile = project.objects.fileProperty()

    DownloadTask() {
        def refreshDeps = project.gradle.startParameter.refreshDependencies

        outputs.upToDateWhen {
            !refreshDeps && targetFile.asFile.get().exists()
        }
    }

    @TaskAction
    def run() {
        def action = new DownloadAction(project, this)
        action.src sourceUrl.get()
        action.dest targetFile.asFile.get()
        action.execute()
    }
}
