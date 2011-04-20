/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.connector.gcontacts.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openengsb.core.api.Domain;

public class GcontactsServiceInstanceFactoryTest {

    @Test
    public void testUpdateServiceInstance() throws Exception {
        GcontactsServiceInstanceFactory gsif = new GcontactsServiceInstanceFactory();
        Map<String, String> attributes = new HashMap<String, String>();
        Domain service = gsif.createNewInstance("id");
        gsif.applyAttributes(service, attributes);
        assertThat(service.getInstanceId(), is("id"));
    }

    @Test
    public void testUpdateValidation() throws Exception {
        GcontactsServiceInstanceFactory gsif = new GcontactsServiceInstanceFactory();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("google.user", "user");
        attributes.put("google.password", "pwd");
        GcontactsServiceImpl service = new GcontactsServiceImpl("id");
        gsif.applyAttributes(service, attributes);
        assertThat(service.getGooglePassword(), is("pwd"));
        assertThat(service.getGoogleUser(), is("user"));
    }
}
