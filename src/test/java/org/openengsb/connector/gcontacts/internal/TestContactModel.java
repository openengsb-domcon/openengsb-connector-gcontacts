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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.core.api.model.OpenEngSBModelEntry;
import org.openengsb.domain.contact.models.Contact;
import org.openengsb.domain.contact.models.InformationTypeWithValue;
import org.openengsb.domain.contact.models.Location;

/**
 * represents a contact with all their necessary infos
 */
public class TestContactModel implements Contact {
    private String id;
    private String name;
    private List<InformationTypeWithValue<String>> mails;
    private List<InformationTypeWithValue<String>> homepages;
    private List<InformationTypeWithValue<String>> telephones;
    private List<InformationTypeWithValue<Location>> locations;
    private List<InformationTypeWithValue<Date>> dates;
    private String comment;

    private Map<String, OpenEngSBModelEntry> entries = new HashMap<String, OpenEngSBModelEntry>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<InformationTypeWithValue<String>> getMails() {
        return mails;
    }

    @Override
    public void setMails(List<InformationTypeWithValue<String>> mails) {
        this.mails = mails;
    }

    @Override
    public List<InformationTypeWithValue<String>> getHomepages() {
        return homepages;
    }

    @Override
    public void setHomepages(List<InformationTypeWithValue<String>> homepages) {
        this.homepages = homepages;
    }

    @Override
    public List<InformationTypeWithValue<String>> getTelephones() {
        return telephones;
    }

    @Override
    public void setTelephones(List<InformationTypeWithValue<String>> telephones) {
        this.telephones = telephones;
    }

    @Override
    public List<InformationTypeWithValue<Location>> getLocations() {
        return locations;
    }

    @Override
    public void setLocations(List<InformationTypeWithValue<Location>> locations) {
        this.locations = locations;
    }

    @Override
    public List<InformationTypeWithValue<Date>> getDates() {
        return dates;
    }

    @Override
    public void setDates(List<InformationTypeWithValue<Date>> dates) {
        this.dates = dates;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void addOpenEngSBModelEntry(OpenEngSBModelEntry entry) {
        entries.put(entry.getKey(), entry);
    }

    @Override
    public List<OpenEngSBModelEntry> getOpenEngSBModelEntries() {
        List<OpenEngSBModelEntry> e = new ArrayList<OpenEngSBModelEntry>();
        e.add(new OpenEngSBModelEntry("id", id, id.getClass()));
        e.add(new OpenEngSBModelEntry("name", name, name.getClass()));
        e.add(new OpenEngSBModelEntry("comment", comment, comment.getClass()));
        for (int i = 0; i < mails.size(); i++) {
            InformationTypeWithValue<?> mail = mails.get(i);
            e.add(new OpenEngSBModelEntry("mails" + i + ".key", mail.getKey(), mail.getKey().getClass()));
            e.add(new OpenEngSBModelEntry("mails" + i + ".value", mail.getValue(), mail.getValue().getClass()));
        }
        for (int i = 0; i < homepages.size(); i++) {
            InformationTypeWithValue<?> homepage = homepages.get(i);
            e.add(new OpenEngSBModelEntry("homepages" + i + ".key", homepage.getKey(), homepage.getKey().getClass()));
            e.add(new OpenEngSBModelEntry("homepages" + i + ".value", homepage.getValue(), homepage.getValue()
                .getClass()));
        }
        for (int i = 0; i < telephones.size(); i++) {
            InformationTypeWithValue<?> telephone = telephones.get(i);
            e.add(new OpenEngSBModelEntry("telephones" + i + ".key", telephone.getKey(), telephone.getKey().getClass()));
            e.add(new OpenEngSBModelEntry("telephones" + i + ".value", telephone.getValue(), telephone.getValue()
                .getClass()));
        }
        for (int i = 0; i < dates.size(); i++) {
            InformationTypeWithValue<?> date = dates.get(i);
            e.add(new OpenEngSBModelEntry("dates" + i + ".key", date.getKey(), date.getKey().getClass()));
            e.add(new OpenEngSBModelEntry("dates" + i + ".value", date.getValue(), date.getValue().getClass()));
        }
        for (int i = 0; i < locations.size(); i++) {
            InformationTypeWithValue<Location> location = locations.get(i);
            e.add(new OpenEngSBModelEntry("locations" + i + ".key", location.getKey(), location.getKey().getClass()));
            e.add(new OpenEngSBModelEntry("locations" + i + ".value.address", location.getValue().getAddress(),
                location.getValue().getAddress().getClass()));
            e.add(new OpenEngSBModelEntry("locations" + i + ".value.city", location.getValue().getCity(),
                location.getValue().getCity().getClass()));
            e.add(new OpenEngSBModelEntry("locations" + i + ".value.country", location.getValue().getCountry(),
                location.getValue().getCountry().getClass()));
            e.add(new OpenEngSBModelEntry("locations" + i + ".value.state", location.getValue().getState(),
                location.getValue().getState().getClass()));
            e.add(new OpenEngSBModelEntry("locations" + i + ".value.zip", location.getValue().getZip(),
                location.getValue().getZip().getClass()));
        }
        e.addAll(entries.values());
        return e;
    }

    @Override
    public void removeOpenEngSBModelEntry(String key) {
        entries.remove(key);
    }
}
