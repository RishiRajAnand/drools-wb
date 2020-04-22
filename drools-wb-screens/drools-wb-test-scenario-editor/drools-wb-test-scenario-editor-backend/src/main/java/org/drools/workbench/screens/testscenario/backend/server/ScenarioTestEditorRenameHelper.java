/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.testscenario.backend.server;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.drools.workbench.models.testscenarios.backend.util.ScenarioXMLPersistence;
import org.drools.workbench.models.testscenarios.shared.Scenario;
import org.drools.workbench.screens.testscenario.type.TestScenarioResourceTypeDefinition;
import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.editor.commons.backend.service.helper.RenameHelper;
import org.uberfire.io.IOService;

/**
 * RenameHelper for Test Editor
 */

@ApplicationScoped
public class ScenarioTestEditorRenameHelper implements RenameHelper {

    private IOService ioService;
    private TestScenarioResourceTypeDefinition testScenarioResourceType;
    private CommentedOptionFactory commentedOptionFactory;

    public ScenarioTestEditorRenameHelper() {
        //Zero-parameter constructor for CDI proxies
    }

    @Inject
    public ScenarioTestEditorRenameHelper(final @Named("ioStrategy") IOService ioService,
                                          final TestScenarioResourceTypeDefinition testScenarioResourceType,
                                          final CommentedOptionFactory commentedOptionFactory) {
        this.ioService = ioService;
        this.testScenarioResourceType = testScenarioResourceType;
        this.commentedOptionFactory = commentedOptionFactory;
    }

    @Override
    public boolean supports(final Path destination) {
        return (testScenarioResourceType.accept(destination));
    }

    @Override
    public void postProcess(final Path source,
                            final Path destination) {
        final org.uberfire.java.nio.file.Path _destination = Paths.convert(destination);

        final String content = ioService.readAllString(_destination);

        final Scenario scenario = ScenarioXMLPersistence.getInstance().unmarshal(content);
        scenario.setName(destination.getFileName());

        ioService.write(_destination,
                        ScenarioXMLPersistence.getInstance().marshal(scenario),
                        commentedOptionFactory.makeCommentedOption("File [" + source.toURI() + "] renamed to [" + destination.toURI() + "]."));
    }
}
