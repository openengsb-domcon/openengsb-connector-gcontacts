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

import java.util.List;

import org.openengsb.core.api.model.OpenEngSBModelEntry;
import org.openengsb.domain.contact.models.InformationTypeWithValue;

/**
 * represents an easy way to store informations with values.
 * Examples: key = phone.private ; value = "..."
 *           key = birthday ; value = a date
 *           key = address.work ; value = a location
 *           ...
 */

public class TestInformationTypeWithValue<T> implements InformationTypeWithValue<T> {
    private String key;
    private T value;
    
    public TestInformationTypeWithValue() {
        
    }
    
    public TestInformationTypeWithValue(String key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
    
    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void addOpenEngSBModelEntry(OpenEngSBModelEntry arg0) {
    }

    @Override
    public List<OpenEngSBModelEntry> getOpenEngSBModelEntries() {
        return null;
    }

    @Override
    public void removeOpenEngSBModelEntry(String arg0) {        
    }
}