package org.opendatadiscovery.internal.oddrn

import org.gradle.api.Plugin
import org.gradle.api.Project

class OddrnGeneratePlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        project.extensions.create(Constants.EXTENSION_NAME, OddrnGenerateExtension.class);
        project.tasks.create(Constants.TASK_NAME, OddrnGenerateTask.class);
    }
}
