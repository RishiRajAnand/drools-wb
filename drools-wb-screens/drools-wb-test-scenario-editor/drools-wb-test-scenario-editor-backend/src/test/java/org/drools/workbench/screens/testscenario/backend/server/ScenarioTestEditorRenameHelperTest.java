/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import java.net.URL;

import org.drools.workbench.screens.testscenario.type.TestScenarioResourceTypeDefinition;
import org.guvnor.common.services.backend.util.CommentedOptionFactory;
import org.guvnor.common.services.project.categories.Decision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.base.options.CommentedOption;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScenarioTestEditorRenameHelperTest {

    @Mock
    private IOService ioService;

    @Mock
    private CommentedOptionFactory commentedOptionFactory;

    ScenarioTestEditorRenameHelper helper;

    private TestScenarioResourceTypeDefinition testScenarioResourceType = new TestScenarioResourceTypeDefinition(new Decision());

    @Before
    public void setup() {
        helper = new ScenarioTestEditorRenameHelper(ioService,
                                                    testScenarioResourceType,
                                                    commentedOptionFactory);
    }

    @Test
    public void testScenarioFile() throws Exception {
        final Path pathSource = mock(Path.class);
        final URL scenarioResource = getClass().getResource("testRenameScenario.scenario");
        final Path pathDestination = PathFactory.newPath(scenarioResource.getFile(),
                                                         scenarioResource.toURI().toString());
        when(pathSource.toURI()).thenReturn("default://p0/src/main/resources/MyFile.scenario");

        helper.postProcess(pathSource,
                           pathDestination);

        final ArgumentCaptor<String> scenarioArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(ioService,
               times(1)).write(any(org.uberfire.java.nio.file.Path.class),
                               scenarioArgumentCaptor.capture(),
                               any(CommentedOption.class));

        final String newDrl = scenarioArgumentCaptor.getValue();
        assertNotNull(newDrl);
        assertTrue(newDrl.contains("testRenameScenario"));
    }
}
